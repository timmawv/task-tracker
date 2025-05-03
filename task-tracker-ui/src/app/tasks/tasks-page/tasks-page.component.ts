import {Component, OnInit, TemplateRef} from '@angular/core';
import {TaskService} from '../../services/task.service';
import {BehaviorSubject, map, Observable} from 'rxjs';
import {Task} from '../../../shared/models/Task';
import {TaskState} from '../../../shared/models/TaskState';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {BsModalRef, BsModalService} from 'ngx-bootstrap/modal';
import {FormControl, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-tasks-page',
  standalone: false,
  templateUrl: './tasks-page.component.html',
  styleUrl: './tasks-page.component.css'
})
export class TasksPageComponent implements OnInit {

  //todo надо вынести модальное окно в отдельный компонент, слишком много кода получается
  private tasksSubject = new BehaviorSubject<Task[]>([]);
  tasks$ = this.tasksSubject.asObservable();
  notStartedTasks$!: Observable<Task[]>;
  inProgressTasks$!: Observable<Task[]>;
  finishedTasks$!: Observable<Task[]>;
  selectedTask: Task | null = null;
  protected readonly TaskState = TaskState;
  errorMessage: string = "";

  constructor(private taskService: TaskService, private modalService: BsModalService) { }

  sendCreateTaskForm() {
    const payload = {
      title: this.createTaskForm.value.title,
      description: this.createTaskForm.value.description
    }

    this.taskService.createTask(payload).subscribe({
      next: (task: Task) => {
        const currentTasks = this.tasksSubject.getValue();
        const updatedTasks = [...currentTasks, task];
        this.tasksSubject.next(updatedTasks);
        this.hideModal();
      },
      error: (error) => {
        this.errorMessage = error.message;
      }
    })
  }

  sendEditTaskForm() {
    const payload = {
      id: this.editTaskForm.value.id,
      title: this.editTaskForm.value.title,
      description: this.editTaskForm.value.description
    }

    this.taskService.editTask(payload.id!, payload).subscribe({
      next: (updatedTask: Task) => {
        const currentTasks = this.tasksSubject.getValue();
        const updatedTasks = currentTasks.map(task =>
          task.id === updatedTask.id ? updatedTask : task
        );
        this.tasksSubject.next(updatedTasks);
        this.hideModal();
      },
      error: (error) => {
        this.errorMessage = error.message;
      }
    })
  }

  openModal(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template);
  }

  openModalInfoTask(template: TemplateRef<any>, task: Task) {
    this.modalRef = this.modalService.show(template);
    this.selectedTask = task;
  }

  openModalEditTask(template: TemplateRef<any>, task: Task) {
    this.modalRef = this.modalService.show(template);
    this.editTaskForm.patchValue({
      id: task.id,
      title: task.title,
      description: task.description
    });
  }

  hideModal() {
    this.createTaskForm.reset();
    this.modalRef.hide();
  }

  modalRef!: BsModalRef;

  createTaskForm = new FormGroup({
    title: new FormControl(''),
    description: new FormControl('')
  });

  editTaskForm = new FormGroup({
    id: new FormControl(''),
    title: new FormControl(''),
    description: new FormControl('')
  });

  ngOnInit() {
    this.taskService.getTasks().subscribe(tasks => this.tasksSubject.next(tasks));

    this.notStartedTasks$ = this.tasks$.pipe(
      map(tasks => tasks.filter(task => task.taskState === TaskState.NOT_STARTED))
    );

    this.inProgressTasks$ = this.tasks$.pipe(
      map(tasks => tasks.filter(task => task.taskState === TaskState.IN_PROGRESS))
    );

    this.finishedTasks$ = this.tasks$.pipe(
      map(tasks => tasks.filter(task => task.taskState === TaskState.FINISHED))
    );
  }

  deleteTask(taskId: string) {
    this.taskService.deleteTask(taskId).subscribe({
      next: () => {
        const currentTasks = this.tasksSubject.getValue();
        const updatedTasks = currentTasks.filter(task => task.id !== taskId);
        this.tasksSubject.next(updatedTasks);
        console.log("Task was deleted!")
      },
      error: (error) => {
        console.error(error.message);
      }
    })
  }

  onDrop(event: CdkDragDrop<Task[]>, taskState: TaskState) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      const movedTask = event.previousContainer.data[event.previousIndex];

      // Удаляем задачу из старого массива и добавляем в новый
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );

      // Обновляем состояние задачи на бекенде
      this.taskService.updateTaskState(movedTask.id, taskState).subscribe({
        next: () => {
          movedTask.taskState = taskState;
          const currentTasks = this.tasksSubject.value;
          this.tasksSubject.next([...currentTasks]);
        },
        error: (error) => console.error('Failed to update task', error)
      });
    }
  }
}

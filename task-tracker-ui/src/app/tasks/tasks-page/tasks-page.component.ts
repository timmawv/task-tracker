import {Component, OnInit} from '@angular/core';
import {TaskService} from '../../services/task.service';
import {BehaviorSubject, map, Observable} from 'rxjs';
import {Task} from '../../../shared/models/Task';
import {TaskState} from '../../../shared/models/TaskState';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-tasks-page',
  standalone: false,
  templateUrl: './tasks-page.component.html',
  styleUrl: './tasks-page.component.css'
})
export class TasksPageComponent implements OnInit {

  private tasksSubject = new BehaviorSubject<Task[]>([]);
  tasks$ = this.tasksSubject.asObservable();
  notStartedTasks$!: Observable<Task[]>;
  inProgressTasks$!: Observable<Task[]>;
  finishedTasks$!: Observable<Task[]>;
  protected readonly TaskState = TaskState;

  constructor(private taskService: TaskService) {
  }

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

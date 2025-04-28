import {Component, OnInit} from '@angular/core';
import {TaskService} from '../../services/task.service';
import {map, Observable} from 'rxjs';
import {Task} from '../../../shared/models/Task';
import {TaskState} from '../../../shared/models/TaskState';

@Component({
  selector: 'app-tasks-page',
  standalone: false,
  templateUrl: './tasks-page.component.html',
  styleUrl: './tasks-page.component.css'
})
export class TasksPageComponent implements OnInit {

  tasks$!: Observable<Task[]>;
  notStartedTasks$!: Observable<Task[]>;
  inProgressTasks$!: Observable<Task[]>;
  finishedTasks$!: Observable<Task[]>;

  constructor(private taskService: TaskService) {
  }

  ngOnInit() {
    this.tasks$ = this.taskService.getTasks();

    this.notStartedTasks$ = this.tasks$.pipe(
      map(tasks => tasks.filter(task => task.taskState === TaskState.NOT_STARTED))
    );

    this.inProgressTasks$ = this.tasks$.pipe(
      map(tasks => tasks.filter(task => task.taskState === TaskState.IN_PROGRESS))
    );

    this.finishedTasks$ = this.tasks$.pipe(
      map(tasks => tasks.filter(task => TaskState.FINISHED))
    );
  }
}

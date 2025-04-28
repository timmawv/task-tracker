import {Component, OnInit} from '@angular/core';
import {TaskService} from '../../services/task.service';
import {Observable} from 'rxjs';
import {Task} from '../../../shared/models/Task';

@Component({
  selector: 'app-tasks-page',
  standalone: false,
  templateUrl: './tasks-page.component.html',
  styleUrl: './tasks-page.component.css'
})
export class TasksPageComponent implements OnInit {

  tasks$!: Observable<Task[]>;

  constructor(private taskService: TaskService) {
  }

  ngOnInit() {
    this.tasks$ = this.taskService.getTasks();
  }
}

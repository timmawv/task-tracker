import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Task} from '../../shared/models/Task';
import {TaskState} from '../../shared/models/TaskState';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private apiUrl = "http://localhost:8080/tasks";

  constructor(private http: HttpClient) { }

  public getTasks() {
    return this.http.get<Task[]>(this.apiUrl);
  }

  public updateTaskState(id: string, taskState: TaskState) {
    return this.http.patch(this.apiUrl.concat(id), { taskState: taskState });
  }
}

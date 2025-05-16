import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Task} from '../../shared/models/Task';
import {TaskState} from '../../shared/models/TaskState';
import {BaseService} from './base.service';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TaskService extends BaseService {

  private apiUrl = "http://localhost:8080/tasks";

  constructor(private http: HttpClient) {
    super();
  }

  public getTasks() {
    return this.http.get<Task[]>(this.apiUrl);
  }

  public createTask(payload: any) {
    const headers = this.getStandardHeaders();
    const params = this.getStandardParameters();

    return this.http.post<Task>(this.apiUrl, payload, {headers, params});
  }

  public deleteTask(taskId: string) {
    return this.http.delete(this.apiUrl.concat("/".concat(taskId)));
  }

  public editTask(taskId: string, payload: any) {
    const headers = this.getStandardHeaders();
    const params = this.getStandardParameters();

    return this.http.put<Task>(this.apiUrl.concat("/".concat(taskId)), payload, {headers, params});
  }

  public updateTaskState(taskId: string, taskState: TaskState) {
    const headers = this.getStandardHeaders();
    const params = this.getStandardParameters();

    const payload = {
      taskState: taskState
    }

    return this.http.patch<Task>(this.apiUrl.concat("/".concat(taskId)), payload, {headers, params});
  }
}

import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Task} from '../../shared/models/Task';
import {TaskState} from '../../shared/models/TaskState';
import {BaseService} from './base.service';

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

  public updateTaskState(id: string, taskState: TaskState) {
    const headers = this.getStandardHeaders();
    const params = this.getStandardParameters();

    const payload = {
      taskState: taskState
    }

    return this.http.patch(this.apiUrl.concat("/".concat(id)), payload, {headers, params});
  }
}

import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Task} from '../../shared/models/Task';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private apiUrl = "http://localhost:8080/tasks";

  constructor(private http: HttpClient) { }

  public getTasks() {
    return this.http.get<Task[]>(this.apiUrl).pipe();
  }
}

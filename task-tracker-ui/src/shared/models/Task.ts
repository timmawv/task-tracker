import {TaskState} from './TaskState';

export class Task {
  constructor(public id: string,
              public title: string,
              public description: string,
              public taskState: TaskState,
              public isCompleted: boolean,
              public createdAt: Date,
              public finishedAt: Date) {
  }
}

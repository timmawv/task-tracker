import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TasksPageComponent } from './tasks-page/tasks-page.component';
import {DragDropModule} from '@angular/cdk/drag-drop';



@NgModule({
  declarations: [
    TasksPageComponent
  ],
  imports: [
    CommonModule,
    DragDropModule
  ]
})
export class TasksModule { }

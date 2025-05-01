import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TasksPageComponent } from './tasks-page/tasks-page.component';
import {DragDropModule} from '@angular/cdk/drag-drop';
import { ModalModule } from 'ngx-bootstrap/modal';
import {ReactiveFormsModule} from '@angular/forms';



@NgModule({
  declarations: [
    TasksPageComponent
  ],
  imports: [
    CommonModule,
    DragDropModule,
    ModalModule.forRoot(),
    ReactiveFormsModule
  ]
})
export class TasksModule { }

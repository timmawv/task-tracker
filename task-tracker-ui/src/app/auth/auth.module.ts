import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import { RegisterSuccessComponent } from './register-success/register-success.component';
import {RouterLink} from '@angular/router';

@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent,
    RegisterSuccessComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
    RouterLink
  ],
  exports: [
    LoginComponent,
    RegisterComponent
  ]
})
export class AuthModule {
}

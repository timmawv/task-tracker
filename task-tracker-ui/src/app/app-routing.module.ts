import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './auth/login/login.component';
import {RegisterComponent} from './auth/register/register.component';
import {MainPageComponent} from './blocks/main-page/main-page.component';
import {NotFoundComponent} from './blocks/not-found/not-found.component';

const routes: Routes = [
  { path: '', component: MainPageComponent},
  { path: 'login', component: LoginComponent},
  { path: 'register', component: RegisterComponent},
  { path: '**', component: NotFoundComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {RouterLink, RouterOutlet} from '@angular/router';
import {AuthService} from './services/auth.service';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import {AuthModule} from './auth/auth.module';
import {BlocksModule} from './blocks/blocks.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    RouterOutlet,
    RouterLink,
    AppRoutingModule,
    AuthModule,
    BlocksModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthService, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

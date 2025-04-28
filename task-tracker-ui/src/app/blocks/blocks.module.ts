import { NgModule } from '@angular/core';
import {CommonModule} from '@angular/common';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import {RouterLink} from '@angular/router';
import { MainPageComponent } from './main-page/main-page.component';
import { NotFoundComponent } from './not-found/not-found.component';

@NgModule({
  declarations: [
    HeaderComponent,
    FooterComponent,
    MainPageComponent,
    NotFoundComponent
  ],
  imports: [
    CommonModule,
    RouterLink,
  ],
  exports: [
    HeaderComponent,
    FooterComponent
  ]
})
export class BlocksModule { }

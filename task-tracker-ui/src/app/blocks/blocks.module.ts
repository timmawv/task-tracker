import { NgModule } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
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
    NgOptimizedImage
  ],
  exports: [
    HeaderComponent,
    FooterComponent
  ]
})
export class BlocksModule { }

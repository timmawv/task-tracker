import { NgModule } from '@angular/core';
import {CommonModule} from '@angular/common';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import {RouterLink} from '@angular/router';
import { MainPageComponent } from './main-page/main-page.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { LoadingComponent } from './loading/loading.component';

@NgModule({
  declarations: [
    HeaderComponent,
    FooterComponent,
    MainPageComponent,
    NotFoundComponent,
    LoadingComponent
  ],
  imports: [
    CommonModule,
    RouterLink,
  ],
  exports: [
    HeaderComponent,
    FooterComponent,
    LoadingComponent
  ]
})
export class BlocksModule { }

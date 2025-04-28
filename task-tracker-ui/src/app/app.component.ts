import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {LoaderService} from './services/loader.service';
import {fadeAnimation} from './animations/animation-fade';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css',
  animations: [fadeAnimation]
})
export class AppComponent implements OnInit {
  title = 'task-tracker-ui';

  isLoading$!: Observable<boolean>;

  constructor(private loaderService: LoaderService) {}

  ngOnInit() {
    this.isLoading$ = this.loaderService.loading$;
  }
}

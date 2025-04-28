import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {LoaderService} from '../../services/loader.service';
import {fadeAnimation} from '../../animations/animation-fade';

@Component({
  selector: 'app-loading',
  standalone: false,
  templateUrl: './loading.component.html',
  styleUrl: './loading.component.css',
  animations: [fadeAnimation]
})
export class LoadingComponent implements OnInit {

  isLoading$!: Observable<boolean>;

  constructor(private loaderService: LoaderService) {}

  ngOnInit() {
    this.isLoading$ = this.loaderService.loading$;
  }
}

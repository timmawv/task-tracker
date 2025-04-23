import { Component } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  isLogged = false;

  constructor(private router: Router/*, private authService: AuthService*/) { }

  // ngOnInit() {
  //   this.authService.userStatus$.subscribe((status) => {
  //     this.isLogged = status;
  //   });
  // }

  goToContact() {
    this.router.navigate(['contact']);
  }

  logout() {
    // this.authService.logout();
    localStorage.removeItem('auth_token');
    this.router.navigate(['']);
  }

}

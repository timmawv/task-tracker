import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit{

  isLogged = false;

  constructor(private router: Router, private authService: AuthService) { }

  ngOnInit() {
    this.authService.userStatus$.subscribe((status) => {
      this.isLogged = status;
    });
  }

  logout() {
    this.authService.logout();
    localStorage.removeItem('auth_token');
    this.router.navigate(['']);
  }
}

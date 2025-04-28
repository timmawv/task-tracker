import {Component} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  loading: boolean = false;
  errorMessage: string = "";

  constructor(private http: HttpClient, private authService: AuthService, private router: Router) {
  }

  login(form: any) {

    if (form.valid) {
      this.loading = true;
      const formData = form.value;

      this.http.post('http://localhost:8080/login', formData)
        .subscribe({
          next: (response: any) => {
            this.errorMessage = "";
            this.loading = false;
            const token = response.token;
            localStorage.setItem('auth_token', token);
            this.authService.login();
            this.router.navigate(['/tasks']);
          },
          error: (error) => {
            this.errorMessage = error.message;
            this.loading = false;
          }
        });
    } else {
      console.log('Form is invalid');
    }
  }
}

import {Component, Input} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  loading = false;
  @Input() errorMessage: string = "";

  constructor(private http: HttpClient, private authService: AuthService) {}

  login(form: any) {
    console.log(form);
    if(form.valid) {
      this.loading = true;
      const formData = form.value;

      this.http.post('http://localhost:8080/login', formData).subscribe({
        next: (response: any) => {
          this.errorMessage = "";
          this.loading = false;
          const token = response.message;
          console.log('Token we got ' + token);
          localStorage.setItem('auth_token', token);
          this.authService.login();
        },
        error: (error) => {
          console.error(error)
          this.errorMessage = error.error.message.replace(/\n/g, '<br>');
          this.loading = false;
        }
      });
    } else {
      console.log('Form is invalid');
    }
  }
}

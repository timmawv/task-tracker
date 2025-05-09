import {Component} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  loading: boolean = false;
  errorMessage: string = "";
  isSuccessRegister: boolean = false;
  userEmail: string = "";
  showPassword: boolean = false;
  showConfirmPassword: boolean = false;

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  register(form: any) {

    if (form.valid) {
      this.loading = true;
      const formData = form.value;

      this.http.post('http://localhost:8080/register', formData)
        .subscribe({
          next: (response: any) => {
            this.errorMessage = "";
            this.loading = false;
            this.isSuccessRegister = true;
            this.userEmail = response.email;
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

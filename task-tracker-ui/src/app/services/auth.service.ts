import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {BehaviorSubject, catchError, Observable, throwError} from 'rxjs';
import {HttpErrorResponse, HttpEvent, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {HttpHandler} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService implements HttpInterceptor {

  authUrls = ['/login', '/register'];

  constructor(private router: Router) {
  }

  private userStatus = new BehaviorSubject<boolean>(this.loadFromStorage());
  userStatus$: Observable<boolean> = this.userStatus.asObservable();

  private loadFromStorage(): boolean {
    return localStorage.getItem('isLoggedIn') === 'true';
  }

  login() {
    this.userStatus.next(true);
    localStorage.setItem('isLoggedIn', 'true');
  }

  logout() {
    this.userStatus.next(false);
    localStorage.removeItem('isLoggedIn');
  }

  isLoggedIn(): boolean {
    return this.userStatus.value;
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const authToken = localStorage.getItem('auth_token');

    if (this.authUrls.includes(req.url)) {
      return next.handle(req);
    }
    let modifiedReq = req;

    if (authToken) {
      modifiedReq = req.clone({
        setHeaders: {
          Authorization: `${authToken}`
        }
      });
    }

    //todo пересмотреть это взаимодействие
    return next.handle(modifiedReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          console.error('Error 401: Unauthorized access, token removed');
          alert('Your session is finished please log in');
          localStorage.removeItem('auth_token');
          this.router.navigate(['/login']);
        } else if (error.status === 403) {
          console.error('Error 403');
          alert('Your session is finished please log in');
          this.router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }
}

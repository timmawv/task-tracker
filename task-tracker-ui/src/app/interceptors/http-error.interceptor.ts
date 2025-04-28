import {Injectable} from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpErrorResponse
} from '@angular/common/http';
import {Observable, throwError, TimeoutError, timeout, catchError} from 'rxjs';
import {Router} from '@angular/router';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

  constructor(private router: Router) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      timeout(5000), // глобальный таймаут
      catchError((error: any) => {
        let errorMessage = "Something went wrong please try again";

        if (error instanceof TimeoutError) {
          errorMessage = "Error occurred please try again";
        } else if (error instanceof HttpErrorResponse) {
          if (error.status === 0) {
            errorMessage = "Error with server, please try again letter";
          } else if (error.status === 401) {
            console.error('Error 401: Unauthorized access, token removed');
            alert('Your session is finished please log in');
            localStorage.removeItem('auth_token');
            this.router.navigate(['/login']);
          } else if (error.status === 403) {
            console.error('Error 403');
            alert('Your session has finished please log in');
            this.router.navigate(['/login']);
          } else if (error.error?.message) {
            errorMessage = error.error.message.replace(/\n/g, '<br>');
          } else {
            errorMessage = `Error ${error.status}: ${error.statusText}`;
          }
        }

        console.error('[HTTP Error]', error); // Можно логировать ошибку, отправлять её в мониторинг и т.п.
        return throwError(() => new Error(errorMessage));
      })
    );
  }

}

import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpErrorResponse } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth';

@Injectable()
export class credentialsInterceptor implements HttpInterceptor {
    constructor(private router: Router, private authService: AuthService) {}
    intercept(req: HttpRequest<any>, next: HttpHandler) {
        // Aggiunge withCredentials per inviare i cookie
        const clonedReq = req.clone({ withCredentials: true });

        return next.handle(clonedReq).pipe(
            catchError((error: HttpErrorResponse) => {
                if (error.status === 401) {
                    this.authService.setLoggedIn(false);
                    this.router.navigate(['/login']);
                }
                return throwError(() => error);
            })
        );
    }
}
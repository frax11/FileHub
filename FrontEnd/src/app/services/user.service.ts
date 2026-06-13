import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map, Observable, tap } from 'rxjs';
import { UserModel, LoginRequest, AuthResponse, RegisterRequest } from '../models/user.model';
import { CsrfToken } from '../models/csfr.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = '/api/user';
  private loggedIn = false;
  private csrfToken = '';

  constructor(private http: HttpClient) {}

  initializeCsrf(): Observable<CsrfToken> {
    console.log('GET /api/user/csrf');
    return this.http.get<CsrfToken>(`${this.apiUrl}/csrf`, { withCredentials: true }).pipe(
      tap({
        next: (response) => {
          this.csrfToken = response.token;
          console.log('✅ CSRF token salvato:', this.csrfToken);
        },
        error: (err) => {
          console.error('❌ Errore CSRF:', err);
        },
      }),
    );
  }

  register(user: RegisterRequest): Observable<AuthResponse> {
    const headers = new HttpHeaders({
      'X-XSRF-TOKEN': this.csrfToken,
    });
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, user, {
      headers: headers,
      withCredentials: true,
    });
  }

  login(user: LoginRequest): Observable<AuthResponse> {
    const headers = new HttpHeaders({
      'X-XSRF-TOKEN': this.csrfToken,
    });
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, user, {
      headers: headers,
      withCredentials: true,
    });
  }

  logout(): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/logout`, {});
  }

  delete(): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/delete`, {});
  }

  getCurrentUser(): Observable<any> {
    return this.http.get(`${this.apiUrl}/me`);
  }

  get isLoggedInValue(): boolean {
    return this.loggedIn;
  }

  setLoggedIn(value: boolean): void {
    this.loggedIn = value;
  }
}

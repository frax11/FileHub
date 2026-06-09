import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserModel, LoginRequest, AuthResponse, RegisterRequest } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = '/api/user';
  private loggedIn = false;
  private csrfToken= '';

  constructor(private http: HttpClient) {}

  initializeCsrf(): Observable<any> {
    console.log('GET /api/user/csrf');
    return this.http.get(`${this.apiUrl}/csrf`, { withCredentials: true });
  }

  setCsrfToken(token: string): void {
    this.csrfToken = token;
    console.log('✅ CSRF token salvato:', token);
  }

  register(user: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, user);
  }

  login(user: LoginRequest): Observable<AuthResponse> {
    const headers = new HttpHeaders({
      'X-XSRF-TOKEN': this.csrfToken
    });
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, user ,{headers:headers ,withCredentials: true});
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

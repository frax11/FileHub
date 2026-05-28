import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User, LoginRequest, AuthResponse } from '../models/user';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl = 'http://localhost:8080/api/auth';
    private loggedIn = false;

    constructor(private http: HttpClient) {}

    register(user: User): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/register`, user);
    }

    login(credentials: LoginRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials);
    }

    logout(): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/logout`, {});
    }

    getCurrentUser(): Observable<User> {
        return this.http.get<User>(`${this.apiUrl}/me`);
    }

    isAuthenticated(): boolean {
        return this.loggedIn;
    }

    setLoggedIn(value: boolean): void {
        this.loggedIn = value;
    }
}
// navbar.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
    selector: 'app-navbar',
    standalone: true,
    imports: [CommonModule, RouterLink],
    template: `
        <nav>
            <a routerLink="/home">Home</a>
            <a routerLink="/files">I miei file</a>
            @if (authService.isAuthenticated()) {
                <button (click)="logout()">Logout</button>
            }
            @if (!authService.isAuthenticated()) {
                <a routerLink="/login">Login</a>
            }
        </nav>
    `,
    styles: [`
        nav {
            background: #333;
            padding: 1rem;
            color: white;
        }
        nav a, nav button {
            color: white;
            margin-right: 1rem;
            text-decoration: none;
        }
        nav button {
            background: none;
            border: none;
            cursor: pointer;
        }
    `]
})
export class navbar {
    constructor(public authService: AuthService, private router: Router) {}
    
    logout() {
        this.authService.logout().subscribe(() => {
            this.authService.setLoggedIn(false);
            this.router.navigate(['/login']);
        });
    }
}
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';
import { User } from '../../models/user';

@Component({
    selector: 'app-home',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './home.html',
    styleUrls: ['./home.scss']
})
export class home implements OnInit {
    user: User | null = null;

    constructor(private authService: AuthService, private router: Router) {}

    ngOnInit(): void {
        this.authService.getCurrentUser().subscribe({
            next: (userData) => {
                this.user = userData;
            },
            error: () => {
                this.router.navigate(['/login']);
            }
        });
    }

    logout(): void {
        this.authService.logout().subscribe({
            next: () => {
                this.authService.setLoggedIn(false);
                this.router.navigate(['/login']);
            }
        });
    }
}
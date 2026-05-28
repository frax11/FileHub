import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink],
    templateUrl: './login.html',
    styleUrls: ['./login.scss']
})
export class login {
    email = '';
    password = '';
    errorMessage = '';

    constructor(private authService: AuthService, private router: Router) {}

    onSubmit(): void {
        this.authService.login({ email: this.email, password: this.password }).subscribe({
            next: () => {
                this.authService.setLoggedIn(true);
                this.router.navigate(['/home']);
            },
            error: () => {
                this.errorMessage = 'Email o password non validi';
            }
        });
    }
}
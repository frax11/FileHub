import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';
import { User } from '../../models/user';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink],
    templateUrl: './register.html',
    styleUrls: ['./register.scss']
})
export class register {
    user: User = {
        email: '',
        name: '',
        surname: '',
        password: ''
    };
    errorMessage = '';
    successMessage = '';
    isLoading = false;

    constructor(
        private authService: AuthService, 
        private router: Router
    ) {}

    onSubmit(): void {
        if (this.isLoading) return;
        
        this.errorMessage = '';
        this.successMessage = '';
        this.isLoading = true;

        const userData = {
            name: this.user.name,
            surname: this.user.surname,
            email: this.user.email,
            password: this.user.password
        };

        console.log('Registrazione:', userData);

        this.authService.register(userData).subscribe({
            next: () => {
                this.successMessage = 'Registrazione completata!';
                this.isLoading = false;
                setTimeout(() => this.router.navigate(['/login']), 2000);
            },
            error: (err) => {
                console.error('Errore:', err);
                this.isLoading = false;
                
                if (err.status === 400) {
                    this.errorMessage = err.error?.message || 'Dati non validi. Controlla i campi.';
                } else if (err.status === 409) {
                    this.errorMessage = 'Email già registrata.';
                } else {
                    this.errorMessage = 'Errore durante la registrazione. Riprova.';
                }
            }
        });
    }
}
// src/app/components/login/login.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { UserService } from '../../services/user.service';
import { routes } from '../../app.routes';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrls: ['./login.scss'],
})
export class Login implements OnInit {
  email = '';
  password = '';
  errorMessage = '';
  isLoading = false; // <- flag per sapere se il form è in caricamento
  csrfReady = false; // ← flag per sapere se CSRF è pronto

  constructor(
    private userService: UserService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.userService.initializeCsrf().subscribe({
      next: () => {
        this.csrfReady = true;
      },
      error: (err) => console.error(err),
    });
  }

  async onSubmit(form: NgForm): Promise<void> {
    // 1. Verifica se il form è valido
    if (form.invalid) {
      Object.keys(form.controls).forEach((key) => {
        form.controls[key].markAsTouched();
      });
      this.router.navigate(['home']);
      return;
    }

    // 2. Verifica che CSRF sia pronto (opzionale)
    if (!this.csrfReady) {
      this.errorMessage = 'Attendi, connessione al server in corso...';
      return;
    }

    // 3. Reset errori e attiva loading
    this.errorMessage = '';
    this.isLoading = true;

    console.log('📤 Tentativo login per:', this.email);

    // 4. Chiamata al backend
    this.userService.login({ email: this.email, password: this.password }).subscribe({
      next: (response) => {
        console.log('✅ Login riuscito:', response);
        this.userService.setLoggedIn(true);
        this.isLoading = false;
        this.router.navigate(['/home']);
      },
      error: (err) => {
        console.error('Login fallito:', err);
        this.errorMessage = 'Email o password non validi';
        this.isLoading = false;
      },
    });
  }
}

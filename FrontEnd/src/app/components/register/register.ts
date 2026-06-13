import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router, RouterLink } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.scss',
  standalone: true,
})
export class Register {
  email = '';
  password = '';
  name = '';
  surname = '';
  errorMessage = '';
  isLoading = false;
  csrfReady = false; // ← flag per sapere se CSRF è pronto

  constructor(
    private userService: UserService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.userService.initializeCsrf().subscribe({
      next: (response: any) => {
        console.log('Token ricevuto:', response.token);
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

    console.log(' Tentativo registrazione per:', this.email);

    // 4. Chiamata al backend
    this.userService
      .register({
        email: this.email,
        password: this.password,
        name: this.name,
        surname: this.surname,
      })
      .subscribe({
        next: (response) => {
          console.log(' Registrazzione riuscita:', response);
          this.isLoading = false;
          this.router.navigate(['/home']);
        },
        error: (err) => {
          console.error('Registrazione fallita:', err);
          this.errorMessage = 'Email o password non validi';
          this.isLoading = false;
        },
      });
  }
}

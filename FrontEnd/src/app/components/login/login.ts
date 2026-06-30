import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Api } from '../../services/api';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
})
export class Login {
  private api = inject(Api);
  private router = inject(Router);

  email = '';
  password = '';
  errorMessage = '';

  async onSubmit() {
    this.errorMessage = '';
    const success = await this.api.login(this.email, this.password);

    if (success) {
      this.router.navigate(['/home']);
    } else {
      this.errorMessage = 'Errore di accesso.';
    }
  }
}

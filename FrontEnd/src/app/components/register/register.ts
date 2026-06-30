import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Api } from '../../services/api';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink], // RouterLink è essenziale qui!
  templateUrl: './register.html',
})
export class Register {
  private api = inject(Api);
  private router = inject(Router);

  email = '';
  name = '';
  surname = '';
  password = '';

  errorMessage = '';
  successMessage = '';

  async onSubmit() {
    this.errorMessage = '';
    this.successMessage = '';

    // Chiama il backend per creare l'account
    const success = await this.api.register(this.email, this.name, this.surname, this.password);

    if (success) {
      this.successMessage = 'Registrazione completata! Puoi fare login.';
      setTimeout(() => {
        this.router.navigate(['/login']);
      }, 2000);
    } else {
      this.errorMessage = 'Errore durante la registrazione. Controlla i dati o riprova.';
    }
  }
}

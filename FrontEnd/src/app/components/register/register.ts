import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';
import { RegisterUserDto } from '../../models/register-user-dto';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink], // RouterLink è essenziale qui!
  templateUrl: './register.html',
})
export class Register {
  private api = inject(AuthService);
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

    const userRegister:RegisterUserDto ={
        name:this.name,
        surname:this.surname,
        email:this.email,
        password:this.password,
    };

    const success = await this.api.register(userRegister);

    if (success) {
      this.successMessage = 'Registrazione completata! Puoi fare login.';
      setTimeout(() => {
        this.router.navigate(['/login']);
      }, 2000);
    } else {
      this.errorMessage = 'Errore durante la registrazione. Utente gia registrato / controlla i dati e riprova. ';
    }
  }
}

import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';
import { LoginUserDto } from '../../models/login-user-dto';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
})
export class Login {
  private api = inject(AuthService);
  private router = inject(Router);

  email = '';
  password = '';
  errorMessage = '';

  async onSubmit() {
    this.errorMessage = '';
    const userLogin : LoginUserDto={
      email:this.email,
      password:this.password
    };
    const success = await this.api.login(userLogin);

    if (success) {
      this.router.navigate(['/home']);
    } else {
      this.errorMessage = 'Errore di accesso.';
    }
  }
}

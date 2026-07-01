import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';

// Importazione dei componenti figli per la gestione dei file
import { Upload } from '../upload/upload';
import { FileList } from '../file-list/file-list';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink, Upload, FileList],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  private authService = inject(AuthService);

  // Getter per recuperare l'email dell'utente corrente dal servizio di autenticazione
  get currentUserEmail() {
    return this.authService.currentUserEmail();
  }

  // Metodo per gestire il logout tramite il servizio dedicato
  async onLogout() {
    await this.authService.logout();
  }
}

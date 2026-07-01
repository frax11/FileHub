import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';
import {UpdateUserDto} from '../../models/update-user-dto';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink], // Aggiunto RouterLink per tornare alla home
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile {
  private authService = inject(AuthService);

  updateName = signal<string>('');
  updateSurname = signal<string>('');
  updatePassword = signal<string>('');
  currentPassword = signal<string>('');
  isLoading = signal<boolean>(false);

  get currentUserEmail() {
    return this.authService.currentUserEmail();
  }
  get currentUserName() {
    return this.authService.currentUserName();
  }
  get currentUserSurname() {
    return this.authService.currentUserSurname();
  }

  async onUpdateProfile() {
    this.isLoading.set(true);
    const updateUser: UpdateUserDto = {
      name: this.updateName(),
      surname: this.updateSurname(),
      updatePassword: this.updatePassword(),
      currentPassword: this.currentPassword(),
    };
    const success = await this.authService.update(updateUser);
    if (success) {
      alert('Profilo aggiornato con successo!');
      this.updatePassword.set(''); // Pulisce la password per sicurezza
    } else {
      alert("Errore durante l'aggiornamento del profilo.");
    }
    this.isLoading.set(false);
  }

  async onDeleteAccount() {
    if (
      !confirm(
        "ATTENZIONE: Sei sicuro di voler eliminare definitivamente il tuo account e tutti i tuoi file? L'operazione è irreversibile.",
      )
    ) {
      return;
    }
    this.isLoading.set(true);
    const success = await this.authService.delete();
    this.isLoading.set(false);
  }
}

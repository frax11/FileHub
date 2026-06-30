import { Component, EventEmitter, OnInit, Output, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Api } from '../../services/api';
import { FileEntity } from '../../models/file-entity';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './home.html',
})
export class Home implements OnInit {


  private api = inject(Api);
  private router = inject(Router);

  files = signal<FileEntity[]>([]);
  isLoading = signal<boolean>(true);
  sharingFileId = signal<string | null>(null);
  shareEmailTarget = signal<string>('');

  get currentUserEmail() {
    return this.api.currentUserEmail();
  }

  ngOnInit() {
    this.loadData();
  }

  async loadData() {
    this.isLoading.set(true);
    try {
      const data = await this.api.getFiles();
      this.files.set(data);
    } catch (error) {
      console.error('Errore durante il recupero dei dati:', error);
    } finally {
      this.isLoading.set(false);
    }
  }

  async onUploadFile(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;

    this.isLoading.set(true);
    const success = await this.api.uploadFile(input.files[0]);
    if (success) {
      await this.loadData();
    } else {
      alert("Errore durante l'upload del file.");
      this.isLoading.set(false);
    }
    input.value = '';
  }

  async onDownloadFile(fileId: string, fileName: string) {
    try {
      await this.api.downloadFile(fileId, fileName);
    } catch (e) {
      alert('Impossibile scaricare il file.');
    }
  }

  async onDeleteFile(fileId: string) {
    if (!confirm('Sei sicuro di voler eliminare questo file?')) return;

    this.isLoading.set(true);
    const success = await this.api.deleteFile(fileId);
    if (success) {
      await this.loadData();
    } else {
      alert("Errore durante l'eliminazione del file.");
      this.isLoading.set(false);
    }
  }

  async onShareFile(fileId: string) {
    if (!this.shareEmailTarget() || !this.shareEmailTarget().includes('@')) {
      alert('Inserisci un indirizzo email valido');
      return;
    }

    this.isLoading.set(true);
    const success = await this.api.shareFile(fileId, this.shareEmailTarget());
    if (success) {
      alert('File condiviso con successo!');
      this.sharingFileId.set(null);
      this.shareEmailTarget.set('');
    } else {
      alert('Impossibile condividere il file.');
    }
    this.isLoading.set(false);
  }

  async onLogout() {
    await this.api.logout();
    await this.router.navigate(['/login']);
  }

  formatSize(bytes: number): string {
    return (bytes / 1024 / 1024).toFixed(2);
  }
}

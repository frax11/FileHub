import { Component, OnInit, signal, inject, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth-service';
import { FileService } from '../../services/file-service';
import { FileEntity } from '../../models/file-entity';

@Component({
  selector: 'app-file-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './file-list.html',
  styleUrl: './file-list.css',
})
export class FileList implements OnInit {
  private authService = inject(AuthService);
  private fileService = inject(FileService);

  files = signal<FileEntity[]>([]);
  isLoading = signal<boolean>(true);

  sharingFileId = signal<string | null>(null);
  shareEmailTarget = signal<string>('');
  shareLimit = signal<number>(100);
  searchQuery = signal<string>('');
  filteredFiles = computed(() => {
    const query = this.searchQuery();
    if (!query) return this.files();
    try {
      const regex = new RegExp(query, 'i');
      return this.files().filter((f) => regex.test(f.fileName) || regex.test(f.ownerEmail));
    } catch (e) {
      const lowerQuery = query.toLowerCase();
      return this.files().filter(
        (f) =>
          f.fileName.toLowerCase().includes(lowerQuery) ||
          f.ownerEmail.toLowerCase().includes(lowerQuery),
      );
    }
  });

  get currentUserEmail() {
    return this.authService.currentUserEmail();
  }

  ngOnInit() {
    this.loadData();
  }

  async loadData() {
    this.isLoading.set(true);
    try {
      const data = await this.fileService.getFiles();
      this.files.set(data);
    } catch (error) {
      console.error('Errore dati:', error);
    } finally {
      this.isLoading.set(false);
    }
  }

  async onDownloadFile(fileId: string, fileName: string) {
    try {
      await this.fileService.downloadFile(fileId, fileName);
    } catch (error) {
      alert('Errore download.');
    } finally {
      await this.loadData();
    }
  }

  async viewSharedFile(fileId: string, fileName: string) {
    try {
      await this.fileService.viewSharedFile(fileId, fileName);
    } catch (error) {
      alert('Errore download.');
    } finally {
      await this.loadData();
    }
  }

  async onDeleteFile(fileId: string) {
    if (!confirm('Eliminare file?')) return;
    var success;
    try {
      success = await this.fileService.deleteFile(fileId);
    } catch (error) {
      alert('Errore eliminzaione.');
    }
    if (success) {
      await this.loadData();
    } else {
      alert('Errore eliminazione.');
      await this.loadData();
    }
  }

  async onShareFile(fileId: string, limit: number) {
    if (!this.shareEmailTarget().includes('@')) return alert('Email non valida');
    let finalLimit = limit > 0 ? limit : 100;

    const success = await this.fileService.shareFile(fileId, this.shareEmailTarget(), finalLimit);
    if (success) {
      alert('Condiviso!');
      this.sharingFileId.set(null);
      this.shareEmailTarget.set('');
      this.shareLimit.set(100);
      await this.loadData();
    } else {
      alert('Errore.');
    }
  }

  async onRevokeAccess(id: string) {
    if (!confirm('Eliminare file?')) return;
    var success;
    try {
      success = await this.fileService.revokeMyAccess(id);
    } catch (error) {
      alert('Errore eliminzaione.');
    }
    if (success) {
      await this.loadData();
    } else {
      alert('Errore eliminazione.');
      await this.loadData();
    }
  }

  isSharable(fileName: string): boolean {
    if (!fileName) return false;
    const ext = fileName.split('.').pop()?.toLowerCase();
    const allowedExtensions = ['pdf', 'png', 'jpg', 'jpeg', 'txt', 'mp4'];
    return allowedExtensions.includes(ext || '');
  }

  formatSize(bytes: number): string {
    return (bytes / 1024 / 1024).toFixed(2);
  }
}

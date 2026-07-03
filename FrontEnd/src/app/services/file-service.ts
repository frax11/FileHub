import { Injectable, inject } from '@angular/core';
import { AuthService } from './auth-service';
import { FileEntity } from '../models/file-entity';

@Injectable({
  providedIn: 'root',
})
export class FileService {
  private apiUrl = 'http://localhost:8080/api';
  private auth = inject(AuthService);

  private fetchOptions: RequestInit = {
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' },
  };

  async getFiles(): Promise<FileEntity[]> {
    const res = await this.auth.customFetch(`${this.apiUrl}/files/get/all`, {
      credentials: 'include',
    });
    if (!res.ok) throw new Error('Errore caricamento file');
    return await res.json();
  }

  async uploadFile(file: File): Promise<boolean> {
    const formData = new FormData();
    formData.append('file', file);
    const res = await this.auth.customFetch(`${this.apiUrl}/files/upload`, {
      method: 'POST',
      credentials: 'include',
      body: formData,
    });
    return res.ok;
  }

  async downloadFile(fileId: string, fileName: string): Promise<void> {
    const res = await this.auth.customFetch(`${this.apiUrl}/files/get/${fileId}`, {
      credentials: 'include',
    });
    if (!res.ok) throw new Error('Errore download');

    const blob = await res.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = fileName;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  }

  async deleteFile(fileId: string): Promise<boolean> {
    const res = await this.auth.customFetch(`${this.apiUrl}/files/delete/${fileId}`, {
      method: 'DELETE',
      credentials: 'include',
    });
    return res.ok;
  }

  async shareFile(
    fileId: string,
    targetEmail: string,
    maxAccessCount: number = 100,
  ): Promise<boolean> {
    const res = await this.auth.customFetch(`${this.apiUrl}/shared/add`, {
      ...this.fetchOptions,
      method: 'POST',
      body: JSON.stringify({
        id: fileId,
        maxAccessCount: maxAccessCount,
        shareTo: targetEmail,
      }),
    });
    return res.ok;
  }
  async viewSharedFile(fileId: string, fileName: string): Promise<void> {
    const res = await this.auth.customFetch(`${this.apiUrl}/shared/get/${fileId}`, {
      credentials: 'include',
    });
    if (!res.ok) throw new Error('Errore caricamento file');
    const blob = await res.blob();
    let ext = 'application/pdf';
    const filename = fileName.toLowerCase();

    if (filename.endsWith('.png')) ext = 'image/png';
    else if (filename.endsWith('.jpg') || filename.endsWith('.jpeg')) ext = 'image/jpeg';
    else if (filename.endsWith('.txt')) ext = 'text/plain';
    else if (filename.endsWith('.mp4')) ext = 'video/mp4';

    const fileBlob = new Blob([blob], { type: ext });

    const url = window.URL.createObjectURL(fileBlob);
    window.open(url, '_blank');

    setTimeout(() => window.URL.revokeObjectURL(url), 1000);

  }
  async revokeMyAccess(fileId: string): Promise<boolean> {
    const res = await this.auth.customFetch(`${this.apiUrl}/shared/revoke/${fileId}`, {
      method: 'DELETE',
      credentials: 'include',
    });
    return res.ok;
  }
}

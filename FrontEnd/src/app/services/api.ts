import { Injectable, signal } from '@angular/core';
import { FileEntity } from '../models/file-entity';

@Injectable({
  providedIn: 'root',
})
export class Api {
  private apiUrl = 'http://localhost:8080/api';

  private fetchOptions: RequestInit = {
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' },
  };

  currentUserEmail = signal<string | null>(null);

  async checkSession(): Promise<boolean> {
    try {
      const res = await fetch(`${this.apiUrl}/user/me`, { credentials: 'include' });
      if (res.ok) {
        const principal = await res.json();
        this.currentUserEmail.set(principal.username || 'Utente');
        return true;
      }
      return false;
    } catch {
      console.log('Errore nella ricezione dati');
      return false;
    }
  }

  async login(email: string, password: string): Promise<boolean> {
    try {
      const res = await fetch(`${this.apiUrl}/user/login`, {
        ...this.fetchOptions,
        method: 'POST',
        body: JSON.stringify({ email, password }),
      });

      if (res.ok) {
        await this.checkSession();
        return true;
      }
      return false;
    } catch (e) {
      console.error('Errore di rete durante il login:', e);
      return false;
    }
  }
  async register(email: string, name: string, surname: string, password: string): Promise<boolean> {
    try {
      const res = await fetch(`${this.apiUrl}/user/register`, {
        ...this.fetchOptions,
        method: 'POST',
        body: JSON.stringify({ email, name,surname,password }),
      });

      if (res.ok) {
        await this.checkSession();
        return true;
      }
      return false;
    } catch (e) {
      console.error('Errore di rete durante il login:', e);
      return false;
    }
  }

  async logout(): Promise<void> {
    try {
      await fetch(`${this.apiUrl}/user/logout`, {
        method: 'POST',
        credentials: 'include',
      });
    } catch (e) {
      console.error("Errore di rete durante il logout, ma forzo l'uscita locale:", e);
    } finally {
      this.currentUserEmail.set(null);
    }
  }

  async getFiles(): Promise<FileEntity[]> {
    const res = await fetch(`${this.apiUrl}/files/get/all`, { credentials: 'include' });
    if (!res.ok) throw new Error('Errore caricamento file');
    return await res.json();
  }

  async uploadFile(file: File): Promise<boolean> {
    const formData = new FormData();
    formData.append('file', file);

    const res = await fetch(`${this.apiUrl}/files/upload`, {
      method: 'POST',
      credentials: 'include',
      body: formData, // Niente Content-Type per i FormData
    });
    return res.ok;
  }

  async downloadFile(fileId: string, fileName: string): Promise<void> {
    const res = await fetch(`${this.apiUrl}/files/get/${fileId}`, { credentials: 'include' });
    if (!res.ok) throw new Error('Errore download');

    const blob = await res.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = fileName; // Diamo al file il suo nome originale
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  }

  async deleteFile(fileId: string): Promise<boolean> {
    const res = await fetch(`${this.apiUrl}/files/delete/${fileId}`, {
      method: 'POST', // Il tuo backend usa PostMapping per eliminare
      credentials: 'include',
    });
    return res.ok;
  }

  async shareFile(fileId: string, targetEmail: string): Promise<boolean> {
    const res = await fetch(`${this.apiUrl}/share/add`, {
      ...this.fetchOptions,
      method: 'POST',
      body: JSON.stringify({
        id: fileId,
        maxAccessCount: 100,
        ownerEmail: targetEmail
      }),
    });
    return res.ok;
  }
}

import { Injectable, signal, inject } from '@angular/core';
import { Router } from '@angular/router';
import { UpdateUserDto } from '../models/update-user-dto';
import { RegisterUserDto } from '../models/register-user-dto';
import { LoginUserDto } from '../models/login-user-dto';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api';
  private router = inject(Router);

  private fetchOptions: RequestInit = {
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' },
  };

  currentUserEmail = signal<string | null>(null);
  currentUserName = signal<string | null>(null);
  currentUserSurname = signal<string | null>(null);

  async customFetch(url: string, options?: RequestInit): Promise<Response> {
    const res = await fetch(url, options);
    if (res.status === 401 || res.status === 403) {
      if(!this.currentUserEmail === null)
        alert('Sessione scaduta');
      this.currentUserEmail.set(null);
      this.router.navigate(['/login']);
    }
    return res;
  }

  async checkSession(): Promise<boolean> {
    try {
      const res = await this.customFetch(`${this.apiUrl}/user/me`, { credentials: 'include' });
      if (res.ok) {
        const principal = await res.json();
        this.currentUserEmail.set(principal.email || 'Utente');
        this.currentUserName.set(principal.name || 'Utente');
        this.currentUserSurname.set(principal.surname || 'Utente');
        return true;
      }
      return false;
    } catch {
      return false;
    }
  }

  async login(body:LoginUserDto): Promise<boolean> {
    try {
      const res = await fetch(`${this.apiUrl}/user/login`, {
        ...this.fetchOptions,
        method: 'POST',
        body: JSON.stringify(body),
      });

      if (res.ok) {
        await this.checkSession();
        return true;
      }
      return false;
    } catch (e) {
      return false;
    }
  }

  async register(body: RegisterUserDto): Promise<boolean> {
    try {
      const res = await fetch(`${this.apiUrl}/user/register`, {
        ...this.fetchOptions,
        method: 'POST',
        body: JSON.stringify(body),
      });
      if(res.status!=200)
        return false;
      alert("Registrazione Effettuata");
      return res.ok;
    } catch (e) {
      return false;
    }
  }

  async update( body: UpdateUserDto): Promise<boolean> {
    try {

      const res = await this.customFetch(`${this.apiUrl}/user/update`, {
        ...this.fetchOptions,
        method: 'POST',
        credentials: 'include',
        body: JSON.stringify(body),
      });
      await this.checkSession();
      return res.ok;
    } catch (e) {
      return false;
    }
  }
  async logout(): Promise<void> {
    try {
      await this.customFetch(`${this.apiUrl}/user/logout`, {
        method: 'POST',
        credentials: 'include',
      });
    } catch (e) {
      console.error('Errore di rete durante il logout', e);
    } finally {
      this.currentUserEmail.set(null);
      this.router.navigate(['/login']);
    }
  }
  async delete() {
    try {
      await this.customFetch(`${this.apiUrl}/user/delete`, {
        method: 'POST',
        credentials: 'include',
      });
    } catch (e) {
      console.error('Errore di rete durante l eliminazione', e);
    } finally {
      this.currentUserEmail.set(null);
      this.router.navigate(['/login']);
    }
  }
}

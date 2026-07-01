import { Component, signal, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, Router } from '@angular/router';
import { AuthService } from './services/auth-service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  template: `
    <div class="min-h-screen bg-gray-200 p-4 font-mono text-black">
      @if (isCheckingSession()) {
        <div class="p-8 text-center font-bold text-xl uppercase">Verifica Sessione...</div>
      } @else {
        <router-outlet></router-outlet>
      }
    </div>
  `,
})
export class App implements OnInit {
  private api = inject(AuthService);
  private router = inject(Router);

  isCheckingSession = signal<boolean>(true);

  async ngOnInit() {
    const isAuthenticated = await this.api.checkSession();
    this.isCheckingSession.set(false);

    if (isAuthenticated) {
      this.router.navigate(['/home']);
    } else {
      if (window.location.pathname !== '/register') {
        this.router.navigate(['/login']);
      }
    }
  }
}

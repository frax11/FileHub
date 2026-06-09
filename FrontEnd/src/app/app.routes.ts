import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },

  {
    path: 'login',
    loadComponent: () => import('./components/login/login').then((m) => m.Login),
  },

  {
    path: 'register',
    loadComponent: () => import('./components/register/register').then((m) => m.Register),
  },

  {
    path: 'home',
    loadComponent: () => import('./components/home/home').then((m) => m.Home),
  },
  { path: '**', redirectTo: '/login' },
];

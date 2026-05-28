import { Routes } from '@angular/router';
import { register } from './components/register/register';
import { login } from './components/login/login';
import { home } from './components/home/home';

export const routes: Routes = [
    { path: '', redirectTo: '/login', pathMatch: 'full' },
    { path: 'register', component: register },
    { path: 'login', component: login },
    { path: 'home', component: home },
    { path: '**', redirectTo: '/login' }
];
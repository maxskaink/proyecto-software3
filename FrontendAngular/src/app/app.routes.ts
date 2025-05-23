import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { AsignatureComponent } from './components/asignature/asignature.component';
import { LoginComponent } from './components/login/login.component';
import {AuthGuard} from "./guard/auth.guard";

export const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'login', component: LoginComponent},
  {path: 'access-denied', component: LoginComponent, data: { accessDenied: true }},
  {path: 'home', component: HomeComponent, canActivate: [AuthGuard], data: { roles: ['Teacher', 'Coordinator'] }},
  {path: 'asignatures/:id', component: AsignatureComponent, canActivate: [AuthGuard]}
];

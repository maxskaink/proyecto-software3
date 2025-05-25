import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { AsignatureComponent } from './components/asignature/asignature.component';
import { LoginComponent } from './components/login/login.component';
import { SettingsComponent } from './components/settings/settings.component';
import { SettingsCompProgramComponent } from './components/settings-comp-program/settings-comp-program.component';
import { SettingsAsignatureComponent } from './components/settings-asignature/settings-asignature.component';

export const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'login', component: LoginComponent},
  {path: 'access-denied', component: LoginComponent, data: { accessDenied: true }},
  {path: 'home', component: HomeComponent},
  {path: 'asignatures/:id', component: AsignatureComponent},
  {path: 'settings', component:SettingsComponent},
  {path: 'settings/competencyProgram', component: SettingsCompProgramComponent},
  {path: 'settings/asignatures', component: SettingsAsignatureComponent}
];

import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { SubjectComponent } from './components/asignature/subject.component';
import { LoginComponent } from './components/login/login.component';
import { SettingsComponent } from './components/settings/settings.component';
import { SettingsCompProgramComponent } from './components/settings-comp-program/settings-comp-program.component';
import { SettingsAsignatureComponent } from './components/settings-asignature/settings-asignature.component';
import { SettingsTeachersComponent } from './components/settings-teachers/settings-teachers.component';
import { OutcomeComponent } from './components/outcome/outcome.component';
import { CompetencyComponent } from './components/competency/competency.component';
import { ProgramCompetencyComponent } from './components/program-competency/program-competency.component';

export const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'login', component: LoginComponent},
  {path: 'access-denied', component: LoginComponent, data: { accessDenied: true }},
  {path: 'home', component: HomeComponent},
  {path: 'asignatures/:id', component: SubjectComponent},
  {path: 'settings', component:SettingsComponent},
  {path: 'settings/competencyProgram', component: SettingsCompProgramComponent},
  {path: 'settings/competencyProgram/create', component: ProgramCompetencyComponent},
  {path: 'settings/asignatures', component: SettingsAsignatureComponent},
  {path: 'settings/teacher', component: SettingsTeachersComponent},
  {path: 'home/outcomes', component: OutcomeComponent},
  {path: 'home/subject/competencySubject/create', component: CompetencyComponent},
];

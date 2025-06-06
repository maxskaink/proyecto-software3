import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { SubjectComponent } from './components/subject/subject.component';
import { LoginComponent } from './components/login/login.component';
import { SettingsComponent } from './components/settings/settings.component';
import { SettingsCompProgramComponent } from './components/settings-comp-program/settings-comp-program.component';
import { SettingsAsignatureComponent } from './components/settings-asignature/settings-asignature.component';
import { SettingsTeachersComponent } from './components/settings-teachers/settings-teachers.component';
import { OutcomeComponent } from './components/outcome/outcome.component';
import { SubjectCompetencyComponent } from './components/subject-competency-create/subject-competency-create.component';
import { ProgramCompetencyComponent } from './components/program-competency/program-competency.component';
import { RequiredParamsGuard } from './guards/required-params.guard';
import { CreateRubricComponent } from './components/create-rubric/create-rubric.component';
import { AssignTeachersComponent } from './components/assign-teachers/assign-teachers.component';
import {TeacherCreateComponent} from "./components/teacher-create/teacher-create.component";
import { AssignEvaluatorsComponent } from './components/assign-evaluators/assign-evaluators.component';
import { RoleGuard } from './guards/role.guard';

export const routes: Routes = [
  {path: '', component: LoginComponent, canActivate: [() => RoleGuard(['coordinator', 'teacher'])]},
  {path: 'login', component: LoginComponent, canActivate: [() => RoleGuard(['coordinator', 'teacher'])]},
  {path: 'access-denied', component: LoginComponent, data: { accessDenied: true }},
  {path: 'home', component: HomeComponent, canActivate: [() => RoleGuard([ 'teacher'])]},
  {path: 'asignatures/:id', component: SubjectComponent},
  {path: 'asignatures/:id/assignTeachers', component: AssignTeachersComponent},
  {path: 'asignatures/:id/subjectCompetency', component: SubjectCompetencyComponent,
    canActivate: [RequiredParamsGuard]},
  {path: 'settings', component:SettingsComponent},
  {path: 'settings/competencyProgram', component: SettingsCompProgramComponent},
  {path: 'settings/competencyProgram/create', component: ProgramCompetencyComponent},
  {path: 'settings/asignatures', component: SettingsAsignatureComponent},
  {path: 'settings/teacher', component: SettingsTeachersComponent},
  {path: 'home/subject/competency/subject/outcome', component: OutcomeComponent},
  {path: 'home/subject/competencySubject/create', component: SubjectCompetencyComponent},
  {path: 'home/subject/competencySubject/outcome/create', component: CreateRubricComponent},
  {path: 'settings/teacher/create', component: TeacherCreateComponent},
  {path: 'settings/teacher/update/:id', component: TeacherCreateComponent},
  {path: 'home/subject/competencySubject/outcome/assignEvaluators', component: AssignEvaluatorsComponent},
];

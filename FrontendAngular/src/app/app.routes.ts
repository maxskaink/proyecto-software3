import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { AsignatureComponent } from './components/asignature/asignature.component';
import { LoginComponent } from './components/login/login.component';
import { authGuard } from './guard/auth.guard';




export const routes: Routes = [
    {path: '', component:LoginComponent},
    {path: 'home',component: HomeComponent,   canActivate: [authGuard], data: ['Teacher', 'Coordinator']},
    { path: 'asignatures/:id', component:AsignatureComponent,   canActivate: [authGuard] }


];

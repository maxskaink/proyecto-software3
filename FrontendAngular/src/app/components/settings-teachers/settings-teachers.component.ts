import { CommonModule } from '@angular/common';
import {Component, OnInit} from '@angular/core';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecules/molecule-back-header/molecule-back-header.component';
import { TemplateListTeachersComponent } from '../../componentsShared/templates/template-list-teachers/template-list-teachers.component';
import { MoleculeAsideMenuComponent } from '../../componentsShared/molecules/molecule-aside-menu/molecule-aside-menu.component';
import {AuthService} from "../../services/auth.service";
import {TeacherDTO} from "../../models/TeacherDTO";
import {Router, Routes} from "@angular/router";

@Component({
  selector: 'app-settings-teachers',
  imports: [CommonModule, MoleculeBackHeaderComponent,
    TemplateListTeachersComponent, MoleculeAsideMenuComponent],
  templateUrl: './settings-teachers.component.html',
  styleUrl: './settings-teachers.component.css'
})
export class SettingsTeachersComponent implements OnInit{
  options = [
    { title: 'Listar docente', description: 'Visualizar docentes' },
    { title: 'Crear docentes', description: 'Crear docentes' }
  ];

  asideMenuItems = [
    { title: 'Listar docentes', action: () => this.listTeachers(), active: true },
    { title: 'Crear docentes', action: () => this.createTeacher() },
  ];

  teachers: TeacherDTO[] =[];

  constructor(private authService: AuthService, private routes: Router) {
  }

  ngOnInit(): void {
   this.listTeachers();
  }

  listTeachers(): void {
    this.authService.getAllUsers().subscribe({
      next: (data) => {
        this.teachers = data;
      },
      error: (error) => {
        console.error('Error fetching teachers:', error);
      },
    });
  }

  createTeacher(): void {
    this.routes.navigate(['/settings/teacher/create']).then();
  }

  goToOption(titleOption: string): void{
    if(titleOption.toLowerCase().includes('listar')) {
      const element = document.getElementById('listTeachersSection');
      if (element) {
        element.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }
    }else if(titleOption.toLowerCase().includes('crear')) {
      this.createTeacher();
    }
  }
}


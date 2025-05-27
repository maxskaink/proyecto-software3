import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecule-back-header/molecule-back-header.component';
import { TemplateListTeachersComponent } from '../../componentsShared/template-list-teachers/template-list-teachers.component';
import { MoleculeAsideMenuComponent } from '../../componentsShared/molecule-aside-menu/molecule-aside-menu.component';

@Component({
  selector: 'app-settings-teachers',
  imports: [CommonModule, MoleculeBackHeaderComponent, 
    TemplateListTeachersComponent, MoleculeAsideMenuComponent],
  templateUrl: './settings-teachers.component.html',
  styleUrl: './settings-teachers.component.css'
})
export class SettingsTeachersComponent {
  options = [
    { title: 'Listar docente', description: 'Visualizar docentes' },
    { title: 'Crear docentes', description: 'Crear docentes' }
  ];

  asideMenuItems = [
    { title: 'Listar docentes', action: () => this.listTeachers() },
    { title: 'Crear docentes', action: () => this.createTeacher() },
  ];

  listTeachers(): void {
    console.log('Listar docentes selected');
  }

  createTeacher(): void {
    console.log('Crear docentes selected');
  }
}


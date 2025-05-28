import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecule-back-header/molecule-back-header.component';
import { TemplateListboxCompleteComponent, TextBlock } from '../../componentsShared/template-listbox-complete/template-listbox-complete.component';
import { TemplateInputBoxtextComponent } from '../../componentsShared/template-input-boxtext/template-input-boxtext.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-settings-asignature',
  imports: [CommonModule, MoleculeBackHeaderComponent, TemplateListboxCompleteComponent, TemplateInputBoxtextComponent],
  templateUrl: './settings-asignature.component.html',
  styleUrl: './settings-asignature.component.css'
})
export class SettingsAsignatureComponent {
  periodo: string = '';
  textBlock: TextBlock[] = [
    {
      title: 'Crear asignatura',
      description: 'Crea una asignatura nueva para este periodo',
      route: ''
    },
    {
      title: 'Reutlizar asignatura',
      description: 'reutiliza una asignatura del periodo pasado',
      route: ''
    }
  ];

  constructor(private router: Router) {}

  handleBlockClick(block: TextBlock): void {
    if (block.title.toLowerCase().includes('crear')) {
      document.getElementById('createAsignature')?.scrollIntoView({ behavior: 'smooth' });
    } else if (block.route) {
      this.router.navigate([block.route]); 
    }
  }
}
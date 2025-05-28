import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MoleculeSectionOptionComponent } from '../../componentsShared/molecule-section-option/molecule-section-option.component';
import { TemplateHeaderTitleComponent } from '../../componentsShared/template-header-title/template-header-title.component';

@Component({
  selector: 'app-outcome',
  imports: [CommonModule,
     FormsModule,
     MoleculeSectionOptionComponent,
     TemplateHeaderTitleComponent],
  templateUrl: './outcome.component.html',
  styleUrl: './outcome.component.css'
})
export class OutcomeComponent {
  options = [
    {
    title: 'RA1',
    description: 'Materia orientada al servicio web revisando diferentes framewoks tales como angular o Django, pasando por fronted y backend junto con su documentación  ',
    showButtonOne: true,
    buttonTextOne: 'Editar descripcion',
    showButtonTwo: false,
  },
  {
    title: 'Evaluadores',
    description: 'Toda RA tiene sus propios evaluadores, ¡Dales un vistazo!',
    showButtonOne: true,
    buttonTextOne: 'Ver evaluadores',
    showButtonTwo: true,
    buttonTextTwo: 'Gestionar Evaluadores'
  },
  {
    title: 'Rubrica',
    description: 'Nuestras rubricas se manejan por criterios y niveles, los criterios te indicaran  el tema evaluado mientras que los niveles que tanto los domina',
    showButtonOne: true,
    buttonTextOne: 'Ver rubrica',
    showButtonTwo: true,
    buttonTextTwo: 'Editar rubrica'
  },
];
  
  handleBotonUno() {
    console.log('Se hizo clic en el botón 1');
    // Aquí puedes hacer redirección, abrir un modal, etc.
  }
  
  handleBotonDos() {
    console.log('Se hizo clic en el botón 2');
  }
}

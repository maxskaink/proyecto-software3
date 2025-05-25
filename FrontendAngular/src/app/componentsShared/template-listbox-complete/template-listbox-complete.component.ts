import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-template-listbox-complete',
  imports: [CommonModule],
  templateUrl: './template-listbox-complete.component.html',
  styleUrl: './template-listbox-complete.component.css'
})
export class TemplateListboxCompleteComponent {
  textBlock= [
    {
      title: 'lisstar competencias',
      description: 'En esta sección se encontrarán las competencias de programa que hayas creado'
    },
    {
      title: 'Crear competencias',
      description: 'Crea las competencias de programa que necesites'
    },
  ];
}

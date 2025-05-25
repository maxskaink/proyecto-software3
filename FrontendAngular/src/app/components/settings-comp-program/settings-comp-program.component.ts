import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { TemplateBlocksSettingsComponent } from '../../componentsShared/template-blocks-settings/template-blocks-settings.component';
import { TemplateListboxCompleteComponent } from '../../componentsShared/template-listbox-complete/template-listbox-complete.component';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecule-back-header/molecule-back-header.component';
import { TemplateBlockGridComponent } from '../../componentsShared/template-block-grid/template-block-grid.component';
import { ProgramCompetencyService } from '../../services/program-competency.service';
import { ProgramCompetency } from '../../models/ProgramCompetency';
import { subscribe } from 'diagnostics_channel';

@Component({
  selector: 'app-settings-comp-program',
  imports: [CommonModule, TemplateListboxCompleteComponent, TemplateBlockGridComponent,MoleculeBackHeaderComponent],
  templateUrl: './settings-comp-program.component.html',
  styleUrl: './settings-comp-program.component.css'
})
export class SettingsCompProgramComponent implements OnInit {
  listCompetency: ProgramCompetency[] = [];
  textBlock= [
    {
      title: 'listar competencias',
      description: 'En esta sección se encontrarán las competencias de programa que hayas creado'
    },
    {
      title: 'Crear competencias',
      description: 'Crea las competencias de programa que necesites'
    },
  ];
  constructor(private serviceProgCompetency: ProgramCompetencyService) {}

  ngOnInit(): void {
    //this.getCompetency();
  }

  getCompetency(): void {
    this.serviceProgCompetency.getAll().subscribe({
      next: (data) => {
        this.listCompetency = data;
        console.log('Competencias cargadas:', data);
      },
      error: (error) => {
        console.error('Error al obtener competencias:', error);
      }
    });
  }
}

import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

// Services
import { ProgramCompetencyService } from '../../services/program-competency.service';

// Models
import { ProgramCompetency } from '../../models/ProgramCompetencyDTO';
import { TextBlock } from '../../componentsShared/templates/template-listbox-complete/template-listbox-complete.component';

// Components
import { TemplateListboxCompleteComponent } from '../../componentsShared/templates/template-listbox-complete/template-listbox-complete.component';
import { TemplateBlockGridComponent } from '../../componentsShared/templates/template-block-grid/template-block-grid.component';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';

@Component({
  selector: 'app-settings-comp-program',
  imports: [
    CommonModule, 
    TemplateListboxCompleteComponent, 
    TemplateBlockGridComponent, 
    TemplateHeaderTitleComponent
  ],
  templateUrl: './settings-comp-program.component.html',
  styleUrl: './settings-comp-program.component.css'
})
export class SettingsCompProgramComponent implements OnInit {
  listCompetency: ProgramCompetency[] = [];
  loading = false;
  error: string | null = null;
  
  textBlock: TextBlock[] = [
    {
      title: 'Listar competencias',
      description: 'En esta sección se encontrarán las competencias de programa que hayas creado'
    },
    {
      title: 'Crear competencias',
      description: 'Crea las competencias de programa que necesites'
    },
  ];

  constructor(
    private serviceProgCompetency: ProgramCompetencyService, 
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getCompetency();
  }

  getCompetency(): void {
    this.loading = true;
    this.error = null;
    
    this.serviceProgCompetency.getAll().subscribe({
      next: (data) => {
        this.listCompetency = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al obtener competencias:', error);
        this.error = 'No se pudieron cargar las competencias';
        this.loading = false;
      }
    });
  }

  handleBlockClick(block: TextBlock): void {
    if (block.title.toLowerCase().includes('listar')) {
      const element = document.getElementById('createAsignature');
      if (element) {
        element.scrollIntoView({ behavior: 'smooth' });
      }
    } else if (block.route) {
      this.router.navigate([block.route]);
    }
  }
}
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MoleculeBlockComponent } from '../../componentsShared/molecules/molecule-block/molecule-block.component';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecules/molecule-back-header/molecule-back-header.component';
import { TemplateBlocksSettingsComponent } from '../../componentsShared/templates/template-blocks-settings/template-blocks-settings.component';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';

@Component({
  selector: 'app-settings',
  imports: [CommonModule, TemplateBlocksSettingsComponent, TemplateHeaderTitleComponent],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent {
  configBlocks = [
    {
      title: 'Programa',
      description: 'En esta sección podrás listar y crear competencias de programa',
      route: 'settings/competencyProgram'
    },
    {
      title: 'Asignatura',
      description: 'En esta sección podrás crear competencias de asignatura',
      route: 'settings/asignatures'
    },
    {
      title: 'Docentes',
      description: 'En esta sección podrás listar y crear competencias de docentes',
      route: 'settings/teacher'
    }
  ];
}

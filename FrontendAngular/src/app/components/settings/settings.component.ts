import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MoleculeBlockComponent } from '../../componentsShared/molecule-block/molecule-block.component';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecule-back-header/molecule-back-header.component';
import { TemplateBlocksSettingsComponent } from '../../componentsShared/template-blocks-settings/template-blocks-settings.component';

@Component({
  selector: 'app-settings',
  imports: [CommonModule, TemplateBlocksSettingsComponent, MoleculeBackHeaderComponent],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent {
  configBlocks = [
    {
      title: 'Programa',
      description: 'En esta sección podrás listar y crear competencias de programa'
    },
    {
      title: 'Asignatura',
      description: 'En esta sección podrás listar y crear competencias de asignatura'
    },
    {
      title: 'Docentes',
      description: 'En esta sección podrás listar y crear competencias de docentes'
    }
  ];
}

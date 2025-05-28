import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MoleculeBlockConfigurationComponent } from '../molecule-block-configuration/molecule-block-configuration.component';

export interface ConfigBlock {
  title: string;
  description: string;
  route: string;
}
@Component({
  selector: 'app-template-blocks-settings',
  imports: [CommonModule, MoleculeBlockConfigurationComponent],
  templateUrl: './template-blocks-settings.component.html',
  styleUrl: './template-blocks-settings.component.css'
})
export class TemplateBlocksSettingsComponent {
  @Input() configBlocks: ConfigBlock[] = [];
}

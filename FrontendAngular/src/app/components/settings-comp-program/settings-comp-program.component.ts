import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { TemplateBlocksSettingsComponent } from '../../componentsShared/template-blocks-settings/template-blocks-settings.component';
import { TemplateListboxCompleteComponent } from '../../componentsShared/template-listbox-complete/template-listbox-complete.component';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecule-back-header/molecule-back-header.component';
import { TemplateBlockGridComponent } from '../../componentsShared/template-block-grid/template-block-grid.component';

@Component({
  selector: 'app-settings-comp-program',
  imports: [CommonModule, TemplateListboxCompleteComponent, TemplateBlockGridComponent,MoleculeBackHeaderComponent],
  templateUrl: './settings-comp-program.component.html',
  styleUrl: './settings-comp-program.component.css'
})
export class SettingsCompProgramComponent {

}

import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { TemplateBlocksSettingsComponent } from '../../componentsShared/template-blocks-settings/template-blocks-settings.component';
import { TemplateListboxCompleteComponent } from '../../componentsShared/template-listbox-complete/template-listbox-complete.component';

@Component({
  selector: 'app-settings-comp-program',
  imports: [CommonModule, TemplateListboxCompleteComponent],
  templateUrl: './settings-comp-program.component.html',
  styleUrl: './settings-comp-program.component.css'
})
export class SettingsCompProgramComponent {

}

import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MoleculeBlockCompetencyComponent } from '../molecule-block-competency/molecule-block-competency.component';

@Component({
  selector: 'app-template-grid-competency-program',
  imports: [CommonModule, FormsModule, MoleculeBlockCompetencyComponent],
  templateUrl: './template-grid-competency-program.component.html',
  styleUrl: './template-grid-competency-program.component.css'
})
export class TemplateGridCompetencyProgramComponent {
  @Input() listCompetency: any;
  
}

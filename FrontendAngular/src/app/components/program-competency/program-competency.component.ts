import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TemplateSelectInputBoxtextComponent } from '../../componentsShared/templates/template-select-input-boxtext/template-select-input-boxtext.component';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';
import { MoleculeOutComeComponent } from '../../componentsShared/molecules/molecule-out-come/molecule-out-come.component';
import { CommonModule } from '@angular/common';
import { ProgramOutcome } from '../../models/ProgramOutcomeDTO';
import { ProgramCompetencyService } from '../../services/program-competency.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-program-competency',
  imports: [CommonModule, TemplateSelectInputBoxtextComponent,
    TemplateHeaderTitleComponent, MoleculeOutComeComponent, FormsModule],
  templateUrl: './program-competency.component.html',
  styleUrl: './program-competency.component.css'
})
export class ProgramCompetencyComponent  {
  // Program competency specific properties
  outcomeDescription: string = '';
  outcomeCreated: boolean = false;
  selectLabelPlaceholder = 'Aquí puedes seleccionar el nivel de la competencia del programa';
  selectPlaceholder = 'Selecciona el nivel de competencia';
  title: string = 'Competencia de programa';
  levels: string[] = ["Basico", "Intermedio", "Avanzado"];
  emptyOutcome: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private programCompetencyService: ProgramCompetencyService,
  ) { }


  setOutcomeDescription(text:string): string {
    this.outcomeDescription = text;
    this.emptyOutcome = false; // Reset empty outcome flag when description is set
    return this.outcomeDescription;
  }


  save(data: { description: string, option: string }): boolean {
    if (!data.description.trim() || data.option === '' ) {
      console.error('Description or selected option is invalid');
      return false;
    }

    if(this.outcomeDescription.trim() === '') {
      this.emptyOutcome = true;
      console.error('Outcome description is empty');
      return false;
    }

    // Format for program competency creation
    const competencyData = {
      description: data.description,
      level: data.option.toLowerCase(),
      programOutcome: {
        description: this.outcomeDescription // You can change this if outcome description is different
      }
    };

    console.log('Creating program competency with data:', competencyData);

    this.programCompetencyService.create(competencyData)
      .subscribe({
        next: (result) => {
          console.log('Program competency assigned successfully:', result);
          this.outcomeCreated = true;
          return true;
        },
        error: (error) => {
          console.error('Error assigning program competency:', error);
          return false;
        }
      });

    return false;
  }
}
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TemplateSelectInputBoxtextComponent } from '../../componentsShared/templates/template-select-input-boxtext/template-select-input-boxtext.component';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';
import { MoleculeOutComeComponent } from '../../componentsShared/molecules/molecule-out-come/molecule-out-come.component';
import { CommonModule } from '@angular/common';
import { ProgramOutcome } from '../../models/ProgramOutcomeDTO';
import { ProgramCompetencyService } from '../../services/program-competency.service';
import { FormsModule } from '@angular/forms';
import {
  MoleculeBackHeaderComponent
} from "../../componentsShared/molecules/molecule-back-header/molecule-back-header.component";
import {AlertmessageComponent} from "../../componentsShared/messages/alertmessage/alertmessage.component";

@Component({
  selector: 'app-program-competency',
  imports: [CommonModule, TemplateSelectInputBoxtextComponent,
    MoleculeOutComeComponent,
    FormsModule,
    MoleculeBackHeaderComponent,
    AlertmessageComponent],
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

  messageAlert: string = 'Competencia de programa creada correctamente';
  stateAlert: 'save' | 'error' | 'correct' = 'save';
  showAlert: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private programCompetencyService: ProgramCompetencyService,
  ) { }

  handleCloseAlert(){
    this.showAlert=false;
  }

  handleSavedAlert(){
    this.messageAlert = "Competencia guardada con exito";
    this.stateAlert = 'save';
    this.showAlert=true;
  }

  handleErrorAlert(error:string){
    this.messageAlert = "Competencia no se ha podido crear: "+ error;
    this.stateAlert = 'error';
    this.showAlert=true;
  }

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
          this.handleSavedAlert()
          return true;
        },
        error: (error) => {
          console.log(error)
          this.handleErrorAlert(error.error)
          return false;
        }
      });

    return false;
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TemplateInputBoxtextComponent } from '../../componentsShared/templates/template-input-boxtext/template-input-boxtext.component';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';
import { MoleculeOutComeComponent } from '../../componentsShared/molecules/molecule-out-come/molecule-out-come.component';
import { CommonModule } from '@angular/common';
import { ProgramOutcomeService } from '../../services/program-outcome.service';
import { SubjectOutomeService } from '../../services/subject_outcome.service';
import { SubjectOutcome } from '../../models/SubjectOutcomeDTO';
import { ProgramOutcome } from '../../models/ProgramOutcomeDTO';
import { SubjectCompetencyService } from '../../services/subject_competency.service';
import { SubjectCompetency } from '../../models/SubjectCompetencyDTO';
import { ProgramCompetency } from '../../models/ProgramCompetencyDTO';
import { ProgramCompetencyService } from '../../services/program-competency.service';
import { FormsModule } from '@angular/forms';

//This component handles both general competency data and subject-specific competency data 
// based on the presence of a subjectId in the query parameters.

@Component({
  selector: 'app-competency',
  imports: [CommonModule, TemplateInputBoxtextComponent,
    TemplateHeaderTitleComponent, MoleculeOutComeComponent, FormsModule],
  templateUrl: './competency.component.html',
  styleUrl: './competency.component.css'
})
export class CompetencyComponent implements OnInit {
  subjectId?: number;
  isSubjectSpecific = false;

  subjectOutcomes: SubjectOutcome[] = [];
  subjectCompetencies: SubjectCompetency[] = [];
  newProgramOutcome: ProgramOutcome = {
    id: 0,
    description: '',
  };
  maxOutcomes: number = 1;
  outcomeCreated: boolean = false;
  selectedOption: string = ''; // Default option
  selectPlaceholder: string = '';

  constructor(
    private route: ActivatedRoute,
    private subjectOutcomeService: SubjectOutomeService,
    private subjectCompetencyService: SubjectCompetencyService,
    private programCompetencyService: ProgramCompetencyService,
  ) { }

  ngOnInit(): void {
    // Get the subjectId from query parameters
    this.route.queryParams.subscribe(params => {
      if (params['subjectId']) {
        this.subjectId = +params['subjectId'];
        this.isSubjectSpecific = true;
        this.loadSubjectSpecificData();
        this.selectPlaceholder = 'Selecciona la competencia del programa a la que pertenecera';
      }
      else {
        this.isSubjectSpecific = false;
        this.selectPlaceholder = 'Seleccciona el nivel de competencia';
      }
    });
  }

  loadSubjectSpecificData(): void {
    if (!this.isSubjectSpecific || !this.subjectId) {
      return;
    }

    this.subjectOutcomeService.getOutcomesBySubject(this.subjectId).subscribe({
      next: (data) => {
        this.subjectOutcomes = data;
        console.log('Subject-specific outcomes loaded:', data);
      },
      error: (err) => {
        console.error('Error loading subject-specific outcomes:', err);
      }
    });
  }

  hasMaxCompetencies(): boolean {
    if (this.isSubjectSpecific) {
      return this.subjectOutcomes.length >= this.maxOutcomes;
    } else {
      return this.outcomeCreated;
    }
  }

  getTitle(): string {
    return this.isSubjectSpecific ? 'Competencia de asignatura' : 'Competencia de programa';
  }

  getOptions(): string[] {
    if (!this.isSubjectSpecific) {
      return ["Basico", "Intermedio", "Avanzado"];
    }
    return [];
  }
  
  save(description: string): boolean {
    if (!description.trim() || this.selectedOption === '') {
      console.error('Description or selected option is invalid');
      return false;
    }
  
    // If the component is subject-specific, we need to handle the subject competency
    if (this.isSubjectSpecific) {
      if (!this.subjectId) {
        console.error("Subject ID is missing");
        return false;
      }
  
      // Create the request body with nested competency and outcome objects
      const requestData = {
        competency: {
          description: description,
          level: "basico", // Could be removed
          programCompetencyId: 1 // Replace with actual program competency ID
        },
        outcome: {
          description: "Outcome description" // TODO: REPLACE WITH CREATE RA
        }
      };
  
      this.subjectCompetencyService.assignCompetencyToSubject(this.subjectId, requestData)
        .subscribe({
          next: (result) => {
            console.log('Competency assigned successfully:', result);
            this.loadSubjectSpecificData();
            return true;
          },
          error: (error) => {
            console.error('Error assigning competency:', error);
            return false;
          }
        });
    } else {
      // Format for program competency creation
      const competencyData = {
        description: description,
        level: this.selectedOption.toLowerCase(),
        programOutcome: {
          description: description // You can change this if outcome description is different
        }
      };
  
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
    }
  
    return false;
  }
}
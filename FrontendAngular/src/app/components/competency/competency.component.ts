import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TemplateSelectInputBoxtextComponent } from '../../componentsShared/templates/template-select-input-boxtext/template-select-input-boxtext.component';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';
import { MoleculeOutComeComponent } from '../../componentsShared/molecules/molecule-out-come/molecule-out-come.component';
import { CommonModule } from '@angular/common';
import { SubjectOutomeService } from '../../services/subject_outcome.service';
import { SubjectOutcome } from '../../models/SubjectOutcomeDTO';
import { ProgramOutcome } from '../../models/ProgramOutcomeDTO';
import { SubjectCompetencyService } from '../../services/subject_competency.service';
import { SubjectCompetency } from '../../models/SubjectCompetencyDTO';
import { ProgramCompetencyService } from '../../services/program-competency.service';
import { FormsModule } from '@angular/forms';

//This component handles both general competency data and subject-specific competency data 
// based on the presence of a programCompetencyId in the query parameters.

@Component({
  selector: 'app-competency',
  imports: [CommonModule, TemplateSelectInputBoxtextComponent,
    TemplateHeaderTitleComponent, MoleculeOutComeComponent, FormsModule],
  templateUrl: './competency.component.html',
  styleUrl: './competency.component.css'
})
export class CompetencyComponent implements OnInit {
  programCompetencyId?: number;
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
  selectPlaceholder: string = '';
  selectLabelPlaceholder: string = '';

  constructor(
    private route: ActivatedRoute,
    private subjectOutcomeService: SubjectOutomeService,
    private subjectCompetencyService: SubjectCompetencyService,
    private programCompetencyService: ProgramCompetencyService,
  ) { }


  ngOnInit(): void {
    // Get both subjectId and programCompetencyId from query parameters
    this.route.queryParams.subscribe(params => {
      // Check for subjectId first
      if (params['subjectId'] && params['programCompetencyId']) {
        this.subjectId = +params['subjectId'];
        this.isSubjectSpecific = true;
        this.programCompetencyId = +params['programCompetencyId'];

        this.loadSubjectSpecificData();
        this.selectLabelPlaceholder = 'Aquí puedes seleccionar la competencia del programa a la que pertenecerá';
        this.selectPlaceholder = 'Selecciona la competencia del programa a la que pertenecerá';
      }
      else {
        this.isSubjectSpecific = false;
        this.selectLabelPlaceholder = 'Aquí puedes seleccionar el nivel de la competencia del programa';
        this.selectPlaceholder = 'Selecciona el nivel de competencia';
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

  save(data: { description: string, option: string }): boolean {
    if (!data.description.trim() || data.option === '') {
      console.error('Description or selected option is invalid');
      return false;
    }

    // If the component is subject-specific, we need to handle the subject competency
    if (this.isSubjectSpecific) {
      if (!this.programCompetencyId) {
        console.error("Subject ID is missing");
        return false;
      }

      if (!this.subjectId) {
        console.error("Subject ID is missing");
        return false;
      }

      // Create the request body with nested competency and outcome objects
      const requestData = {
        competency: {
          description: data.description,
          level: "basico", // Could be removed
          programCompetencyId: this.programCompetencyId // Replace with actual program competency ID
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
        description: data.description,
        level: data.option.toLowerCase(),
        programOutcome: {
          description: data.description // You can change this if outcome description is different
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
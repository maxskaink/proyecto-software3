import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TemplateSelectInputBoxtextComponent } from '../../componentsShared/templates/template-select-input-boxtext/template-select-input-boxtext.component';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';
import { MoleculeOutComeComponent } from '../../componentsShared/molecules/molecule-out-come/molecule-out-come.component';
import { SubjectOutomeService } from '../../services/subject_outcome.service';
import { SubjectCompetencyService } from '../../services/subject_competency.service';
import { SubjectOutcome } from '../../models/SubjectOutcomeDTO';
import { SubjectCompetency } from '../../models/SubjectCompetencyDTO';
import { ProgramCompetencyService } from '../../services/program-competency.service';

@Component({
  selector: 'app-subject-competency',
  imports: [CommonModule, TemplateSelectInputBoxtextComponent,
    TemplateHeaderTitleComponent, MoleculeOutComeComponent, FormsModule],
  templateUrl: './subject-competency.component.html',
  styleUrl: './subject-competency.component.css'
})
export class SubjectCompetencyComponent implements OnInit {
  programCompetencyId?: number;
  subjectId?: number;
  
  subjectOutcomes: SubjectOutcome[] = [];
  subjectCompetencies: SubjectCompetency[] = [];
  createdOutcomes: SubjectOutcome[] = [];
  maxOutcomes: number = 3;
  selectPlaceholder: string = 'Selecciona la competencia del programa a la que pertenecerá';
  selectLabelPlaceholder: string = 'Aquí puedes seleccionar la competencia del programa a la que pertenecerá';
  programCompetencies: any[] = []; // Add this property

  constructor(
    private route: ActivatedRoute,
    private subjectOutcomeService: SubjectOutomeService,
    private subjectCompetencyService: SubjectCompetencyService,
    private programCompetencyService: ProgramCompetencyService // Add this line
  ) { }

  ngOnInit(): void {
    // Since we have a guard, we can assume these parameters always exist
    this.route.queryParams.subscribe(params => {
      // No need to check if they exist - guard guarantees they do
      this.subjectId = +params['subjectId'];
      this.programCompetencyId = +params['programCompetencyId'];
      this.loadSubjectSpecificData();
      this.loadProgramCompetencies(); // Add this line
    });
  }

  loadProgramCompetencies(): void {
    this.programCompetencyService.getAll().subscribe({
      next: (data) => {
        this.programCompetencies = data;
        console.log('Program competencies loaded:', data);
      },
      error: (err) => {
        console.error('Error loading program competencies:', err);
      }
    });
  }

  loadSubjectSpecificData(): void {
    if (!this.subjectId) {
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
    return this.createdOutcomes.length >= this.maxOutcomes;
  }

  getTitle(): string {
    return 'Competencia de asignatura';
  }

  getOptions(): string[] {
    // Return the descriptions of program competencies
    return this.programCompetencies.map(comp => 
      `${comp.description} (${comp.level || 'Sin nivel'})`
    );
  }

  save(data: { description: string, option: string }): boolean {
    if (!data.description.trim() || data.option === '') {
      console.error('Description or selected option is invalid');
      return false;
    }

    if (!this.programCompetencyId) {
      console.error("Program Competency ID is missing");
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
        programCompetencyId: this.programCompetencyId
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

    return false;
  }
  
  onOutcomeChange(newDescription: string, outcomeId: number): void {
    // Handle outcome editing
    console.log(`Updating outcome ${outcomeId} with: ${newDescription}`);
    // Call service to update the outcome
  }
}
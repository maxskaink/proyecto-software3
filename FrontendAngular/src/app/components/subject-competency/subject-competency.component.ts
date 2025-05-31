import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

// Services
import { SubjectOutomeService } from '../../services/subject_outcome.service';
import { SubjectCompetencyService } from '../../services/subject_competency.service';
import { ProgramCompetencyService } from '../../services/program-competency.service';
import { TermService } from '../../services/term.service';

// Models
import { SubjectOutcome } from '../../models/SubjectOutcomeDTO';
import { SubjectCompetency } from '../../models/SubjectCompetencyDTO';
import { TermDTO } from '../../models/TermDTO';

// Components
import { TemplateSelectInputBoxtextComponent } from '../../componentsShared/templates/template-select-input-boxtext/template-select-input-boxtext.component';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';
import { MoleculeOutComeComponent } from '../../componentsShared/molecules/molecule-out-come/molecule-out-come.component';
import { TemplateModalReuseOutcomeComponent } from '../../componentsShared/templates/template-modal-reuse-outcome/template-modal-reuse-outcome.component';

@Component({
  selector: 'app-subject-competency',
  imports: [
    CommonModule, 
    FormsModule,
    TemplateSelectInputBoxtextComponent,
    TemplateHeaderTitleComponent, 
    MoleculeOutComeComponent
  ],
  templateUrl: './subject-competency.component.html',
  styleUrl: './subject-competency.component.css'
})
export class SubjectCompetencyComponent implements OnInit {
  // Route parameters
  programCompetencyId!: number;
  subjectId!: number;

  // Data collections
  subjectOutcomes: SubjectOutcome[] = [];
  subjectCompetencies: SubjectCompetency[] = [];
  createdOutcomes: SubjectOutcome[] = [];
  programCompetencies: any[] = []; 
  terms: TermDTO[] = [];
  selectedOutcomes: SubjectOutcome[] = [];

  // UI configuration
  maxOutcomes: number = 3;
  selectPlaceholder: string = 'Selecciona la competencia del programa a la que pertenecerá';
  selectLabelPlaceholder: string = 'Aquí puedes seleccionar la competencia del programa a la que pertenecerá';
  modalSelectPlaceholder: string = 'Selecciona el periodo al que pertenece el RA';

  constructor(
    private route: ActivatedRoute,
    private subjectOutcomeService: SubjectOutomeService,
    private subjectCompetencyService: SubjectCompetencyService,
    private programCompetencyService: ProgramCompetencyService,
    private termService: TermService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.subjectId = +params['subjectId'];
      this.programCompetencyId = +params['programCompetencyId'];
      this.loadSubjectSpecificData();
      this.loadTerms();
      this.loadProgramCompetencies();
    });
  }

  loadProgramCompetencies(): void {
    this.programCompetencyService.getAll().subscribe({
      next: (data) => {
        this.programCompetencies = data;
      },
      error: () => {
        // Handle error silently or implement proper error handling
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
      },
      error: () => {
        // Handle error silently or implement proper error handling
      }
    });
  }

  loadTerms(): void {
    this.termService.getTerms().subscribe({
      next: (data) => {
        this.terms = data;
      },
      error: () => {
        // Handle error silently or implement proper error handling
      }
    });
  }

  getTermOptions(): string[] {
    return this.terms.map(term => `${term.description} `);
  }

  hasMaxCompetencies(): boolean {
    return this.createdOutcomes.length >= this.maxOutcomes;
  }

  getTitle(): string {
    return 'Competencia de asignatura';
  }

  getOptions(): string[] {
    if (!this.programCompetencies || this.programCompetencies.length === 0) {
      return [];
    }
    
    return this.programCompetencies.map(comp => 
      `${comp.description} (${comp.level || 'Sin nivel'})`
    );
  }

  save(data: { description: string, option: string }): boolean {
    if (!data.description.trim() || data.option === '' || 
        !this.programCompetencyId || !this.subjectId) {
      return false;
    }

    

    const requestData = {
      competency: {
        description: data.description,
        level: "basico",
        programCompetencyId: this.programCompetencyId
      },
      outcome: {
        description: "Outcome description"
      }
    };

    this.subjectCompetencyService.assignCompetencyToSubject(this.subjectId, requestData)
      .subscribe({
        next: () => {
          this.loadSubjectSpecificData();
        },

      });

    return false;
  }

  openModal() {
    if (!this.subjectId) {
      return;
    }

    const dialogRef = this.dialog.open(TemplateModalReuseOutcomeComponent, {
      width: '700px',
      data: {
        subjectId: this.subjectId,
        options: this.getTermOptions(),
        selectedOutcomes: this.selectedOutcomes,
        selectDescription: this.selectLabelPlaceholder,
        selectPlaceholder: this.modalSelectPlaceholder
      }
    });
  
    dialogRef.afterClosed().subscribe(result => {
      if (result && Array.isArray(result)) {
        this.selectedOutcomes = result;
      }
    });
  }

}
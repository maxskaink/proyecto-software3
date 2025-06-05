import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

// Services
import { SubjectOutomeService } from '../../services/subject_outcome.service';
import { SubjectCompetencyService } from '../../services/subject_competency.service';
import { ProgramCompetencyService } from '../../services/program-competency.service';
import { TermService } from '../../services/term.service';

// Models
import { SubjectOutcome } from '../../models/SubjectOutcomeDTO';
import { SubjectCompetency, SubjectCompetencyPostDTO } from '../../models/SubjectCompetencyDTO';
import { TermDTO } from '../../models/TermDTO';

// Components
import { TemplateSelectInputBoxtextComponent } from '../../componentsShared/templates/template-select-input-boxtext/template-select-input-boxtext.component';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';
import { MoleculeOutComeComponent } from '../../componentsShared/molecules/molecule-out-come/molecule-out-come.component';
import { TemplateModalReuseOutcomeComponent } from '../../componentsShared/templates/template-modal-reuse-outcome/template-modal-reuse-outcome.component';
import { TemplateModalCreateOutcomeComponent } from '../../componentsShared/templates/template-modal-create-outcome/template-modal-create-outcome/template-modal-create-outcome.component';
import {
  MoleculeBackHeaderComponent
} from "../../componentsShared/molecules/molecule-back-header/molecule-back-header.component";
import { AlertmessageComponent } from '../../componentsShared/messages/alertmessage/alertmessage.component';
import { timeout } from 'rxjs';

@Component({
  selector: 'app-subject-competency',
  imports: [
    CommonModule,
    FormsModule,
    TemplateSelectInputBoxtextComponent,
    MoleculeOutComeComponent,
    MoleculeBackHeaderComponent,
    TemplateHeaderTitleComponent,
    AlertmessageComponent
],
  templateUrl: './subject-competency-create.component.html',
  styleUrl: './subject-competency-create.component.css',
})
export class SubjectCompetencyComponent implements OnInit {
  // Route parameters
  subjectId!: number;

  // Data collections
  programCompetencyId: number = -1;
  subjectOutcomes: SubjectOutcome[] = [];
  subjectCompetencies: SubjectCompetency[] = [];
  createdOutcomes: SubjectOutcome[] = [];
  programCompetencies: any[] = [];
  terms: TermDTO[] = [];
  selectedOutcomes: SubjectOutcome[] = [];

  // UI configuration
  maxOutcomes: number = 3;
  selectPlaceholder: string = 'Selecciona la competencia del programa para tu nueva competencia de asignatura';
  selectLabelPlaceholder: string = 'Aquí puedes seleccionar la competencia del programa para tu nueva competencia de asignatura';
  modalSelectPlaceholder: string = 'Selecciona el periodo al que pertenece el RA';
  modalCreateDescription: string = 'Ingresa la descripcion del nuevo resultado de aprendizaje';
  outcomeTouched: boolean = false;

  //Variables for alert
  messageAlert:string = '';
  stateAlert:'save' | 'error' | 'correct'='save';
  showAlert:boolean=false;

  constructor(
    private route: ActivatedRoute,
    private subjectOutcomeService: SubjectOutomeService,
    private subjectCompetencyService: SubjectCompetencyService,
    private programCompetencyService: ProgramCompetencyService,
    private termService: TermService,
    private dialog: MatDialog,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.subjectId = +params['subjectId'];
      this.loadInitialData();
    });
  }

  hideAlert(){
    this.showAlert=false;
  }

  errorAlert(message: string){
    this.messageAlert = `Error creando la competencia: ${message}`;
    this.stateAlert = "error";
    this.showAlert = true;
  }

  saveAlert(){
    this.messageAlert = `Competencia guardada, se redirigira a la asignatura`;
    this.stateAlert = "save";
    this.showAlert = true;
  }

  // Data loading methods
  private loadInitialData(): void {
    this.loadSubjectOutcomes();
    this.loadTerms();
    this.loadProgramCompetencies();
  }

  private loadProgramCompetencies(): void {
    this.programCompetencyService.getAll().subscribe({
      next: (data) => this.programCompetencies = data,
      error: () => {}  // Silent error handling
    });
  }

  private loadSubjectOutcomes(): void {
    if (!this.subjectId) return;

    this.subjectOutcomeService.getOutcomesBySubject(this.subjectId).subscribe({
      next: (data) => this.subjectOutcomes = data,
      error: () => {}  // Silent error handling
    });
  }

  private loadTerms(): void {
    this.termService.getTerms().subscribe({
      next: (data) => this.terms = data,
      error: () => {}  // Silent error handling
    });
  }

  // UI helper methods
  getTermOptions(): string[] {
    return this.terms.map((term) => `${term.description} `);
  }

  hasOutcomes(): boolean {
    return this.selectedOutcomes.length > 0;
  }

  hasMaxOutcomes(): boolean {
    return this.selectedOutcomes.length >= this.maxOutcomes;
  }

  getTitle(): string {
    return 'Competencia de asignatura';
  }

  getOptions(): string[] {
    if (!this.programCompetencies || this.programCompetencies.length === 0) {
      return [];
    }

    return this.programCompetencies.map(
      (comp) => `${comp.description} (${comp.level || 'Sin nivel'})`
    );
  }

  // Main operations
  save(data: { description: string; option: string }): boolean {
    if (!this.validateSaveData(data)) {
      return false;
    }

    const requestData = this.prepareRequestData(data);

    this.subjectCompetencyService
      .assignCompetencyToSubject(this.subjectId, requestData)
      .subscribe({
        next: () => {
          this.loadSubjectOutcomes();
          this.saveAlert();
          setTimeout(()=>{
            this.location.back();
          },3000)
        },
        error: (error) => {
          this.errorAlert(error.error)
        }
      });

    return true;
  }

  private validateSaveData(data: { description: string; option: string }): boolean {
    if (!data.description.trim() || data.option === '' || !this.subjectId) {
      return false;
    }

    const selectedOption = data.option.trim();
    const selectedCompetency = this.findSelectedCompetency(selectedOption);

    if (!selectedCompetency) {
      return false;
    }

    this.programCompetencyId = selectedCompetency.id;

    if (this.selectedOutcomes.length > this.maxOutcomes || this.selectedOutcomes.length === 0) {
      this.outcomeTouched = true;
      return false;
    }

    return true;
  }

  private findSelectedCompetency(selectedOption: string): any {
    return this.programCompetencies.find((comp) => {
      const formattedOption = `${comp.description} (${comp.level || 'Sin nivel'})`;
      return formattedOption === selectedOption;
    });
  }

  private prepareRequestData(data: { description: string; option: string }): SubjectCompetencyPostDTO {
    return {
      competency: {
        id: 0,
        description: data.description,
        programCompetencyId: this.programCompetencyId
      },
      outcomes: this.selectedOutcomes.map(outcome => ({
        id: 0,
        description: outcome.description,
        rubric: null,
        idCompetencyAssignment:0
      }))
    };
  }

  // Outcome management
  removeOutcome(outcome: SubjectOutcome): void {
    const index = this.selectedOutcomes.findIndex(selected => selected.id === outcome.id);
    if (index !== -1) {
      this.selectedOutcomes.splice(index, 1);
    }
  }

  // Modal operations
  openModalReuse() {
    if (!this.subjectId) return;

    const currentSelectedOutcomes = [...this.selectedOutcomes];

    const dialogRef = this.dialog.open(TemplateModalReuseOutcomeComponent, {
      width: '700px',
      data: {
        subjectId: this.subjectId,
        selectedOutcomes: currentSelectedOutcomes,
        selectDescription: this.selectLabelPlaceholder,
        selectPlaceholder: this.modalSelectPlaceholder,
        maxOutcomes: this.maxOutcomes
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result && Array.isArray(result)) {
        this.processReuseModalResult(result);
      }
    });
  }

  private processReuseModalResult(result: SubjectOutcome[]): void {
    // Service outcomes (positive IDs)
    const serviceOutcomes = result.filter(outcome => outcome.id > 0);

    // Created outcomes (negative or zero IDs)
    const createdOutcomesInSelection = this.selectedOutcomes.filter(
      outcome => outcome.id <= 0
    );

    // Combine both sets
    this.selectedOutcomes = [...serviceOutcomes, ...createdOutcomesInSelection];

    // Ensure max outcomes not exceeded
    if (this.selectedOutcomes.length > this.maxOutcomes) {
      this.selectedOutcomes = this.selectedOutcomes.slice(0, this.maxOutcomes);
    }
  }

  openModalCreate() {
    if (!this.subjectId) return;

    const dialogRef = this.dialog.open(TemplateModalCreateOutcomeComponent, {
      width: '700px',
      data: {
        newSubjectOutcome: { description: '' } as SubjectOutcome,
        selectedOutcomes: this.selectedOutcomes,
        textDescription: this.modalCreateDescription,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.description) {
        this.createdOutcomes.push(result);
        this.selectedOutcomes.push(result);
      }
    });
  }
}

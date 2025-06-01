import { ProgramCompetency } from '../../models/ProgramCompetencyDTO';
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
import {SubjectCompetency, SubjectCompetencyPostDTO} from '../../models/SubjectCompetencyDTO';
import { TermDTO } from '../../models/TermDTO';

// Components
import { TemplateSelectInputBoxtextComponent } from '../../componentsShared/templates/template-select-input-boxtext/template-select-input-boxtext.component';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';
import { MoleculeOutComeComponent } from '../../componentsShared/molecules/molecule-out-come/molecule-out-come.component';
import { TemplateModalReuseOutcomeComponent } from '../../componentsShared/templates/template-modal-reuse-outcome/template-modal-reuse-outcome.component';
import { TemplateModalCreateOutcomeComponent } from '../../componentsShared/templates/template-modal-create-outcome/template-modal-create-outcome/template-modal-create-outcome.component';

@Component({
  selector: 'app-subject-competency',
  imports: [
    CommonModule,
    FormsModule,
    TemplateSelectInputBoxtextComponent,
    TemplateHeaderTitleComponent,
    MoleculeOutComeComponent,
  ],
  templateUrl: './subject-competency-create.component.html',
  styleUrl: './subject-competency-create.component.css',
})
export class SubjectCompetencyComponent implements OnInit {
  // Route parameters
  subjectId!: number;

  // Data collections
  ProgramCompetencyId: number = -1;
  subjectOutcomes: SubjectOutcome[] = [];
  subjectCompetencies: SubjectCompetency[] = [];
  createdOutcomes: SubjectOutcome[] = [];
  programCompetencies: any[] = [];
  terms: TermDTO[] = [];
  selectedOutcomes: SubjectOutcome[] = [];

  // UI configuration
  maxOutcomes: number = 3;
  selectPlaceholder: string =
    'Selecciona la competencia del programa a la que pertenecerá';
  selectLabelPlaceholder: string =
    'Aquí puedes seleccionar la competencia del programa a la que pertenecerá';
  modalSelectPlaceholder: string =
    'Selecciona el periodo al que pertenece el RA';
  modalCreateDescription: string =
    'Ingresa la descripcion del nuevo resultado de aprendizaje';

  constructor(
    private route: ActivatedRoute,
    private subjectOutcomeService: SubjectOutomeService,
    private subjectCompetencyService: SubjectCompetencyService,
    private programCompetencyService: ProgramCompetencyService,
    private termService: TermService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.subjectId = +params['subjectId'];
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
      },
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
      },
    });
  }

  loadTerms(): void {
    this.termService.getTerms().subscribe({
      next: (data) => {
        this.terms = data;
      },
      error: () => {
        // Handle error silently or implement proper error handling
      },
    });
  }

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

  save(data: { description: string; option: string }): boolean {
    // Validación básica
    if (!data.description.trim() || data.option === '' || !this.subjectId) {
      return false;
    }

    // Extraer la descripción de la opción seleccionada (quitar la parte del nivel)
    const selectedOption = data.option.trim();
    const selectedCompetency = this.programCompetencies.find((comp) => {
      const formattedOption = `${comp.description} (${
        comp.level || 'Sin nivel'
      })`;
      return formattedOption === selectedOption;
    });

    if (!selectedCompetency) {
      return false;
    }

    // Asignar el ID de la competencia seleccionada
    this.ProgramCompetencyId = selectedCompetency.id;

    // Validaciones de resultados de aprendizaje
    if (this.selectedOutcomes.length >= this.maxOutcomes) {
      return false;
    }

    if (this.selectedOutcomes.length === 0) {
      return false;
    }

    const requestData: SubjectCompetencyPostDTO = {
      competency:{
        id:0,
        description: data.description,
        programCompetencyId: this.ProgramCompetencyId
      },
      outcomes:this.selectedOutcomes.map(outcome=>({
        id:0,
        description:outcome.description,
        rubric:null,
      }))
    };

    this.subjectCompetencyService
      .assignCompetencyToSubject(this.subjectId, requestData)
      .subscribe({
        next: () => {
          this.loadSubjectSpecificData();
        },
        error: () => {
          // Manejar error silenciosamente o implementar manejo de errores adecuado
        },
      });

    return true;
  }

// Corrige el nombre y añade la implementación
toggleOutcomeSelection(outcome: SubjectOutcome): void {
  // Buscar el índice del outcome en la lista de seleccionados
  const index = this.selectedOutcomes.findIndex(selected => selected.id === outcome.id);

  // Si se encuentra, eliminarlo de la lista
  if (index !== -1) {
    this.selectedOutcomes.splice(index, 1);
  }
}

  openModalReuse() {
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
        selectPlaceholder: this.modalSelectPlaceholder,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result && Array.isArray(result)) {
        this.selectedOutcomes = result;
      }
    });
  }

  openModalCreate() {
    if (!this.subjectId) {
      return;
    }

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

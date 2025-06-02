import {
  Component,
  EventEmitter,
  Inject,
  Input,
  Output,
  PLATFORM_ID,
} from '@angular/core';
import { SubjectCompetency } from '../../../models/SubjectCompetencyDTO';
import { SubjectOutcome } from '../../../models/SubjectOutcomeDTO';
import { catchError, Observable, of, tap } from 'rxjs';
import { ProgramCompetency } from '../../../models/ProgramCompetencyDTO';
import { SubjectOutomeService } from '../../../services/subject_outcome.service';
import { CommonModule } from '@angular/common';
import { MoleculeOutComeComponent } from '../../molecules/molecule-out-come/molecule-out-come.component';
import { SubjectCompetencyService } from '../../../services/subject_competency.service';
import { FormsModule } from '@angular/forms';
import { EditStateService } from '../../../services/edit-state.service';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { TemplateModalReuseOutcomeComponent } from '../../templates/template-modal-reuse-outcome/template-modal-reuse-outcome.component';
import { TemplateModalCreateOutcomeComponent } from '../../templates/template-modal-create-outcome/template-modal-create-outcome/template-modal-create-outcome.component';

import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-template-competency-edit',
  imports: [CommonModule, MoleculeOutComeComponent, FormsModule],
  templateUrl: './template-competency-edit.component.html',
  styleUrl: './template-competency-edit.component.css',
})
export class TemplateCompetencyEditComponent {
  @Input() competency!: SubjectCompetency;
  @Input() subjectId!: number;
  @Output() editStateChange = new EventEmitter<boolean>();

  // Competency data
  editedCompetency: SubjectCompetency = {} as SubjectCompetency;
  programCompetency$!: Observable<ProgramCompetency>;
  validationError: string = '';

  // Solo necesitamos dos listas
  originalOutcomes: SubjectOutcome[] = []; // Lista inmutable - estado original
  currentOutcomes: SubjectOutcome[] = []; // Lista de trabajo - lo que se muestra en UI
  outcomes$!: Observable<SubjectOutcome[]>; // Observable para la UI

  // UI configuration
  maxOutcomes: number = 3;
  selectPlaceholder: string =
    'Selecciona la competencia del programa para tu nueva competencia de asignatura';
  selectLabelPlaceholder: string =
    'Aquí puedes seleccionar la competencia del programa para tu nueva competencia de asignatura';
  modalSelectPlaceholder: string =
    'Selecciona el periodo al que pertenece el RA';
  modalCreateDescription: string =
    'Ingresa la descripcion del nuevo resultado de aprendizaje';
  noOutcomesSended: boolean = false;

  constructor(
    private outcomeService: SubjectOutomeService,
    private editStateService: EditStateService,
    private competencyService: SubjectCompetencyService,
    private router: Router,
    private dialog: MatDialog
  ) {}

  private initialized = false;

  ngOnInit(): void {
    if (this.initialized) return;
    this.initialized = true;

    if (this.competency) {
      this.editedCompetency = { ...this.competency };
      this.loadOutcomes();
    }
  }

  loadOutcomes(): void {
    this.outcomes$ = this.outcomeService
      .getOutcomesByCompetency(this.competency.id)
      .pipe(
        tap((outcomes) => {
          this.originalOutcomes = outcomes;
          this.currentOutcomes = [...outcomes]; // Copia superficial para manipulación
          this.outcomes$ = of(this.currentOutcomes); // Convertir a Observable

          // Mover los console.log aquí, después de que los datos estén disponibles
          console.log('Original outcomes:', this.originalOutcomes);
          console.log('Current outcomes:', this.currentOutcomes);
        }),
        catchError((error) => {
          console.error('Error loading outcomes:', error);
          return of([]); // Retornar un observable vacío en caso de error
        })
      );
  }

  hasMaxOutcomes(): boolean {
    return this.currentOutcomes.length >= this.maxOutcomes;
  }

  hasOutcomes(): boolean {
    return this.currentOutcomes.length > 0;
  }

  removeOutcome(outcome: SubjectOutcome): void {
    // Eliminar el outcome de la lista actual
    this.currentOutcomes = this.currentOutcomes.filter(
      (o) => o.id !== outcome.id
    );

    // Actualizar el observable
    this.outcomes$ = of(this.currentOutcomes);

  }

  openModalReuse(): void {
    const currentSelectedOutcomes = [...this.currentOutcomes];
    const dialogRef = this.dialog.open(TemplateModalReuseOutcomeComponent, {
      width: '700px',
      data: {
        subjectId: this.subjectId,
        selectedOutcomes: currentSelectedOutcomes,
        selectDescription: this.selectLabelPlaceholder,
        selectPlaceholder: this.modalSelectPlaceholder,
        maxOutcomes: this.maxOutcomes,
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
    const serviceOutcomes = result.filter((outcome) => outcome.id > 0);

    // Created outcomes (negative or zero IDs)
    const createdOutcomesInSelection = this.currentOutcomes.filter(
      (outcome) => outcome.id <= 0
    );

    // Combine both sets
    this.currentOutcomes = [...serviceOutcomes, ...createdOutcomesInSelection];

    // Ensure max outcomes not exceeded
    if (this.currentOutcomes.length > this.maxOutcomes) {
      this.currentOutcomes = this.currentOutcomes.slice(0, this.maxOutcomes);
    }

    this.outcomes$ = of(this.currentOutcomes); // Actualizar el observable
  }

  openModalCreate(): void {
    const dialogRef = this.dialog.open(TemplateModalCreateOutcomeComponent, {
      width: '700px',
      data: {
        newSubjectOutcome: { description: '' } as SubjectOutcome,
        selectedOutcomes: this.currentOutcomes,
        textDescription: this.modalCreateDescription,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.description) {
        this.currentOutcomes.push(result);
      }
    });

    this.outcomes$ = of(this.currentOutcomes); // Actualizar el observable
  }

  onSaveClick(): void {
    // Validaciones
    if (this.currentOutcomes.length === 0) {
      this.noOutcomesSended = true;
      return;
    }

    if (this.editedCompetency.description.trim() === '') {
      return;
    }

    // 1. Primero actualizar la competencia y SUSCRIBIRNOS al observable
    this.competencyService
      .updateCompetency(this.competency.id, this.editedCompetency)
      .subscribe({
        next: (updatedCompetency) => {
          // 2. Solo después de que la competencia se actualizó correctamente, procesar los outcomes

          // Crear arreglos para almacenar los observables
          const deleteObservables = [];
          const createObservables = [];

          // CASO 2: Outcomes que están en current pero NO están en original -> AGREGAR
          const outcomesToAdd = this.currentOutcomes.filter(
            (currentOutcome) =>
              !this.originalOutcomes.some(
                (originalOutcome) => originalOutcome.id === currentOutcome.id
              )
          );

          console.log('Outcomes a añadir:', outcomesToAdd);
          for (const outcome of outcomesToAdd) {
            // Guardar el observable (sin ejecutarlo aún)
            createObservables.push(
              this.outcomeService.createOutcome(
                this.subjectId,
                this.competency.id,
                outcome
              )
            );
          }

          // CASO 1: Outcomes que están en original pero NO están en current -> ELIMINAR
          const outcomesToRemove = this.originalOutcomes.filter(
            (originalOutcome) =>
              !this.currentOutcomes.some(
                (currentOutcome) => currentOutcome.id === originalOutcome.id
              )
          );

          console.log('Outcomes a eliminar:', outcomesToRemove);
          for (const outcome of outcomesToRemove) {
            // Guardar el observable (sin ejecutarlo aún)
            deleteObservables.push(
              this.outcomeService.deleteOutcome(outcome.id)
            );
          }

          // Combinar todos los observables - PRIMERO CREAR, DESPUÉS ELIMINAR
          const allObservables = [...createObservables, ...deleteObservables];

          if (allObservables.length > 0) {
            // Ejecutar todas las operaciones y suscribirse al resultado combinado
            forkJoin(allObservables).subscribe({
              next: (results) => {
                console.log('Operaciones completadas con éxito:', results);
                // Cerrar el modo edición
                this.editStateChange.emit(false);
                this.editStateService.setEditState(false);
              },
              error: (error) => {
                console.error('Error en operaciones:', error);
                // Podrías mostrar un mensaje de error aquí
              },
            });
          } else {
            // No hay cambios que procesar en outcomes
            console.log('No hay cambios que guardar en outcomes');
            this.editStateChange.emit(false);
            this.editStateService.setEditState(false);
          }
        },
        error: (error) => {
          console.error('Error al actualizar la competencia:', error);
          // Mostrar mensaje de error si es necesario
        },
      });
  }

  onCancelClick(): void {
    this.editStateChange.emit(false);
    this.editStateService.setEditState(false);
  }

  goToOutcome(outcome: SubjectOutcome, index: number): void {
    // Solo navegar a outcomes con ID positivo (guardados en BD)
    if (outcome.id <= 0) return;

    this.router
      .navigate(['/home/subject/competency/subject/outcome'], {
        queryParams: {
          outcomeId: outcome.id,
          competencyId: this.competency.id,
          subjectId: this.subjectId,
        },
      })
      .then(() => {
        console.log('Navegando al outcome:', outcome.id);
      })
      .catch((error) => {
        console.error('Error en navegación:', error);
      });
  }
}

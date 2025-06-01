import { Component, EventEmitter, Inject, Input, Output, PLATFORM_ID } from '@angular/core';
import { SubjectCompetency } from '../../../models/SubjectCompetencyDTO';
import { SubjectOutcome } from '../../../models/SubjectOutcomeDTO';
import { catchError, Observable, of, tap } from 'rxjs';
import { ProgramCompetency } from '../../../models/ProgramCompetencyDTO';
import { SubjectOutomeService } from '../../../services/subject_outcome.service';
import { ProgramCompetencyService } from '../../../services/program-competency.service';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { MoleculeOutComeComponent } from '../../molecules/molecule-out-come/molecule-out-come.component';
import { SubjectCompetencyService } from '../../../services/subject_competency.service';
import { FormsModule } from '@angular/forms';
import { EditStateService } from '../../../services/edit-state.service';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { TemplateModalReuseOutcomeComponent } from '../../templates/template-modal-reuse-outcome/template-modal-reuse-outcome.component';
import { TemplateModalCreateOutcomeComponent } from '../../templates/template-modal-create-outcome/template-modal-create-outcome/template-modal-create-outcome.component';

@Component({
  selector: 'app-template-competency-edit',
  imports: [CommonModule, MoleculeOutComeComponent, FormsModule],
  templateUrl: './template-competency-edit.component.html',
  styleUrl: './template-competency-edit.component.css'
})
export class TemplateCompetencyEditComponent {
    @Input() competency!: SubjectCompetency;
    @Output() editStateChange = new EventEmitter<boolean>();

    editedCompetency: SubjectCompetency = {} as SubjectCompetency;
     
    outcomes$!: Observable<SubjectOutcome[]>;
    programCompetency$!: Observable<ProgramCompetency>;
    validationError: string = '';

    // New properties for modal functionality
    maxOutcomes: number = 3;
    selectedOutcomes: SubjectOutcome[] = [];
    createdOutcomes: SubjectOutcome[] = [];
    modalCreateDescription: string = 'Ingresa la descripcion del nuevo resultado de aprendizaje';
    modalSelectPlaceholder: string = 'Selecciona el periodo al que pertenece el RA';
    
    loading = {
      outcomes: false,
      programCompetency: false
    };
    
    error = {
      outcomes: false,
      programCompetency: false
    };
  
    constructor(
      private outcomeService: SubjectOutomeService, 
      private competencyProgramService: ProgramCompetencyService,
      private editStateService: EditStateService,
      private competencyService: SubjectCompetencyService,
      private router: Router,
      private dialog: MatDialog // Added MatDialog
    ) {
    }
  
    ngOnInit(): void {
      if (this.competency) {
        this.loadOutcomes();
        this.editedCompetency = {
          ...this.competency
        };
        console.log('Outomces loaded:', this.outcomes$);
      }
    }

    loadOutcomes(): void {
      this.loading.outcomes = true;
      this.outcomes$ = this.outcomeService.getOutcomesByCompetency(this.competency.id).pipe(
        tap(outcomes => {
          this.loading.outcomes = false;
          this.selectedOutcomes = [...outcomes]; // Store outcomes for management
        }),
        catchError(error => {
          console.error('Error loading outcomes:', error);
          this.error.outcomes = true;
          this.loading.outcomes = false;
          return of([]);
        })
      );
    }
  
    // ...existing code...

    // New methods for outcome management
    hasMaxOutcomes(): boolean {
      return this.selectedOutcomes.length >= this.maxOutcomes;
    }

    openModalReuse(): void {
      if (!this.competency.id) return;

      const currentSelectedOutcomes = [...this.selectedOutcomes];

      const dialogRef = this.dialog.open(TemplateModalReuseOutcomeComponent, {
        width: '700px',
        data: {
          subjectId: this.competency.id,
          selectedOutcomes: currentSelectedOutcomes,
          selectDescription: 'Selecciona el resultado de aprendizaje a reutilizar',
          selectPlaceholder: this.modalSelectPlaceholder,
          maxOutcomes: this.maxOutcomes
        }, 
      });

      dialogRef.afterClosed().subscribe((result) => {
        if (result && Array.isArray(result)) {
          // Process the results from the reuse modal
          this.processReuseModalResult(result);
          
          // Update the outcomes observable to reflect changes in the UI
          this.outcomes$ = of(this.selectedOutcomes);
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

    openModalCreate(): void {
      if (!this.competency.id) return;

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
          // Add new outcome to tracking arrays
          this.createdOutcomes.push(result);
          this.selectedOutcomes.push(result);
          
          // Update the outcomes observable to reflect changes in the UI
          this.outcomes$ = of(this.selectedOutcomes);
        }
      });
    }

    // Method to remove an outcome if needed
    removeOutcome(outcome: SubjectOutcome): void {
      const index = this.selectedOutcomes.findIndex(selected => selected.id === outcome.id);
      if (index !== -1) {
        this.selectedOutcomes.splice(index, 1);
        // Update the outcomes observable
        this.outcomes$ = of(this.selectedOutcomes);
      }
    }
    
    validateInputs(): boolean {
      if (!this.editedCompetency.description || this.editedCompetency.description.trim() === '') {
        this.validationError = 'La descripción de la competencia es obligatoria.';
        return false;
      }
      if (!this.editedCompetency.programCompetencyId) {
        this.validationError = 'Debe seleccionar una competencia de programa.';
        return false;
      }
      this.validationError = '';
      return true;
    }
    onSaveClick(): void {
      if (!this.validateInputs()) return;

      this.competencyService.updateCompetency(1, this.editedCompetency).subscribe({
        next: () => {
          this.editStateService.setEditState(false);
          this.editStateChange.emit(false);
          },
        error: (error) => {
          console.error('Error updating competency:', error);
        }
      });
    }
    
    onCancelClick(): void {
      this.editStateService.setEditState(false);
      this.editStateChange.emit(false);
    }
    onAddOutcome():void{

    }

    goToOutcome(outcome: SubjectOutcome, index: number): void {
      // Usar el ID real del outcome si está disponible
      const outcomeId = outcome.id;
      
      this.router.navigate(['/home/subject/competency/subject/outcome'], {
          queryParams: {
              outcomeId: outcomeId,
              // Agregar otros parámetros necesarios si los hay
          }
      }).then(() => {
          console.log('Navegando al outcome:', outcomeId);
      }).catch(error => {
          console.error('Error en la navegación:', error);
      });
    }
 

}

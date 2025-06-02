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
    // Input and Output properties
    @Input() competency!: SubjectCompetency;
    @Output() editStateChange = new EventEmitter<boolean>();

    // Competency data
    editedCompetency: SubjectCompetency = {} as SubjectCompetency;
    programCompetency$!: Observable<ProgramCompetency>;
    validationError: string = '';
    
    // Outcome management
    outcomes$!: Observable<SubjectOutcome[]>; // Observable for displaying outcomes in UI
    maxOutcomes: number = 3; // Maximum allowed outcomes
    
    // Separate lists for tracking outcomes
    existingOutcomes: SubjectOutcome[] = []; // Outcomes loaded from database
    newlySelectedOutcomes: SubjectOutcome[] = []; // Outcomes added during editing
    selectedOutcomes: SubjectOutcome[] = []; // Combined list of all outcomes (for display and processing)
    
    // Modal configuration
    modalCreateDescription: string = 'Ingresa la descripcion del nuevo resultado de aprendizaje';
    modalSelectPlaceholder: string = 'Selecciona el periodo al que pertenece el RA';
    
    // Loading and error states
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
      private dialog: MatDialog
    ) { }
  
    // Lifecycle methods
    ngOnInit(): void {
      if (this.competency) {
        this.loadOutcomes();
        this.editedCompetency = { ...this.competency };
        console.log('Outcomes loaded:', this.outcomes$);
      }
    }

    // Data loading methods
    loadOutcomes(): void {
      this.loading.outcomes = true;
      this.outcomes$ = this.outcomeService.getOutcomesByCompetency(this.competency.id).pipe(
        tap(outcomes => {
          this.loading.outcomes = false;
          // Store original outcomes from database
          this.existingOutcomes = [...outcomes];
          // Initialize selected outcomes with existing ones
          this.selectedOutcomes = [...outcomes];
        }),
        catchError(error => {
          console.error('Error loading outcomes:', error);
          this.error.outcomes = true;
          this.loading.outcomes = false;
          return of([]);
        })
      );
    }
  
    // Outcome management methods
    hasMaxOutcomes(): boolean {
      // Check if maximum outcome limit has been reached
      return this.selectedOutcomes.length >= this.maxOutcomes;
    }

    openModalReuse(): void {
      // Open modal for reusing existing outcomes
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
      // Process selected outcomes from reuse modal
      
      // Outcomes from the database (positive IDs)
      const reusedOutcomes = result.filter(outcome => outcome.id > 0);
      
      // Keep track of newly created outcomes (negative or zero IDs)
      const newOutcomes = this.selectedOutcomes.filter(outcome => outcome.id <= 0);
      
      // Update tracking lists
      this.newlySelectedOutcomes = [...reusedOutcomes.filter(
        outcome => !this.existingOutcomes.some(existing => existing.id === outcome.id)
      ), ...newOutcomes];
      
      // Combine existing and newly selected outcomes
      this.selectedOutcomes = [...this.existingOutcomes, ...this.newlySelectedOutcomes];
      
      // Ensure max outcomes not exceeded
      if (this.selectedOutcomes.length > this.maxOutcomes) {
        this.selectedOutcomes = this.selectedOutcomes.slice(0, this.maxOutcomes);
        // Update newly selected outcomes based on what remains in the combined list
        this.newlySelectedOutcomes = this.selectedOutcomes.filter(
          outcome => !this.existingOutcomes.some(existing => existing.id === outcome.id)
        );
      }
    }

    openModalCreate(): void {
      // Open modal for creating new outcomes
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
          // Add new outcome to tracking lists
          this.newlySelectedOutcomes.push(result);
          this.selectedOutcomes = [...this.existingOutcomes, ...this.newlySelectedOutcomes];
          
          // Update the outcomes observable to reflect changes in the UI
          this.outcomes$ = of(this.selectedOutcomes);
        }
      });
    }

    removeOutcome(outcome: SubjectOutcome): void {
      // Remove an outcome from the selected list
      const index = this.selectedOutcomes.findIndex(selected => selected.id === outcome.id);
      
      if (index !== -1) {
        // Check if it's an existing outcome or newly added one
        if (this.existingOutcomes.some(existing => existing.id === outcome.id)) {
          // If it's an existing outcome, remove it from existingOutcomes
          const existingIndex = this.existingOutcomes.findIndex(e => e.id === outcome.id);
          if (existingIndex !== -1) {
            this.existingOutcomes.splice(existingIndex, 1);
          }
        } else {
          // If it's a newly added outcome, remove it from newlySelectedOutcomes
          const newIndex = this.newlySelectedOutcomes.findIndex(n => n.id === outcome.id);
          if (newIndex !== -1) {
            this.newlySelectedOutcomes.splice(newIndex, 1);
          }
        }
        
        // Remove from combined list
        this.selectedOutcomes.splice(index, 1);
        
        // Update the outcomes observable
        this.outcomes$ = of(this.selectedOutcomes);
      }
    }
    
    // Validation and action methods
    validateInputs(): boolean {
      // Validate required fields before saving
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
      // Save competency changes
      if (!this.validateInputs()) return;

      this.competencyService.updateCompetency(this.competency.id, this.editedCompetency).subscribe({
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
      // Cancel editing and restore original state
      this.editStateService.setEditState(false);
      this.editStateChange.emit(false);
    }
    
    onAddOutcome(): void {
      // Placeholder for adding outcomes
      // Implementation depends on specific requirements
    }

    goToOutcome(outcome: SubjectOutcome, index: number): void {
      // Navigate to outcome detail page
      const outcomeId = outcome.id;
      
      this.router.navigate(['/home/subject/competency/subject/outcome'], {
          queryParams: {
              outcomeId: outcomeId,
          }
      }).then(() => {
          console.log('Navigating to outcome:', outcomeId);
      }).catch(error => {
          console.error('Navigation error:', error);
      });
    }
}

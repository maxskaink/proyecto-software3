import {
  Component,
  EventEmitter,
  Input,
  Output,
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
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { TemplateModalReuseOutcomeComponent } from '../../templates/template-modal-reuse-outcome/template-modal-reuse-outcome.component';
import { TemplateModalCreateOutcomeComponent } from '../../templates/template-modal-create-outcome/template-modal-create-outcome/template-modal-create-outcome.component';
import { forkJoin } from 'rxjs';

/**
 * Component for editing subject competencies and their associated learning outcomes.
 * Manages the creation, modification, and deletion of outcomes while maintaining
 * proper constraints (min 1, max 3 outcomes per competency).
 */
@Component({
  selector: 'app-template-competency-edit',
  imports: [CommonModule, MoleculeOutComeComponent, FormsModule],
  templateUrl: './template-competency-edit.component.html',
  styleUrl: './template-competency-edit.component.css',
})
export class TemplateCompetencyEditComponent {
  // Input/Output properties
  @Input() competency!: SubjectCompetency;
  @Input() subjectId!: number;
  @Output() editStateChange = new EventEmitter<boolean>();

  // Competency data
  editedCompetency: SubjectCompetency = {} as SubjectCompetency;
  programCompetency$!: Observable<ProgramCompetency>;
  validationError: string = '';

  // Outcomes management
  originalOutcomes: SubjectOutcome[] = []; // Immutable list - original state
  currentOutcomes: SubjectOutcome[] = []; // Working list - what's shown in UI
  outcomes$!: Observable<SubjectOutcome[]>; // Observable for the UI

  // Configuration
  maxOutcomes: number = 3;
  selectPlaceholder: string = 'Seleccione la competencia del programa para su nueva competencia de asignatura';
  selectLabelPlaceholder: string = 'Aquí puede seleccionar la competencia del programa para su nueva competencia de asignatura';
  modalSelectPlaceholder: string = 'Seleccione el término al que pertenece el resultado de aprendizaje';
  modalCreateDescription: string = 'Ingrese la descripción del nuevo resultado de aprendizaje';
  noOutcomesSended: boolean = false;
  private initialized = false;

  /**
   * Constructor with dependency injection
   */
  constructor(
    private outcomeService: SubjectOutomeService,
    private competencyService: SubjectCompetencyService,
    private router: Router,
    private dialog: MatDialog
  ) {}

  /**
   * Lifecycle hook: Component initialization
   */
  ngOnInit(): void {
    if (this.initialized) return;
    this.initialized = true;

    if (this.competency) {
      this.editedCompetency = { ...this.competency };
      this.loadOutcomes();
    }
  }

  // PUBLIC METHODS - UI INTERACTIONS

  /**
   * Loads learning outcomes for the current competency
   */
  loadOutcomes(): void {
    this.outcomes$ = this.outcomeService
      .getOutcomesByCompetency(this.competency.id)
      .pipe(
        tap((outcomes) => {
          this.originalOutcomes = outcomes;
          this.currentOutcomes = [...outcomes]; // Shallow copy for manipulation
          this.outcomes$ = of(this.currentOutcomes); // Convert to Observable

          console.log('Original outcomes:', this.originalOutcomes);
          console.log('Current outcomes:', this.currentOutcomes);
        }),
        catchError((error) => {
          console.error('Error loading outcomes:', error);
          return of([]); // Return empty observable in case of error
        })
      );
  }

  /**
   * Checks if the maximum number of outcomes has been reached
   * @returns boolean indicating if max outcomes limit is reached
   */
  hasMaxOutcomes(): boolean {
    return this.currentOutcomes.length >= this.maxOutcomes;
  }

  /**
   * Checks if there are any outcomes
   * @returns boolean indicating if there are outcomes
   */
  hasOutcomes(): boolean {
    return this.currentOutcomes.length > 0;
  }

  /**
   * Removes an outcome from the current list
   * @param outcome The outcome to remove
   */
  removeOutcome(outcome: SubjectOutcome): void {
    this.currentOutcomes = this.currentOutcomes.filter(
      (o) => o.id !== outcome.id
    );
    this.outcomes$ = of(this.currentOutcomes);
  }

  /**
   * Opens modal dialog for reusing existing outcomes from other terms
   */
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

  /**
   * Opens modal dialog for creating a new learning outcome
   */
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

    this.outcomes$ = of(this.currentOutcomes); // Update the observable
  }

  /**
   * Handles save button click event
   * Updates competency and processes outcomes
   */
  onSaveClick(): void {
    // Initial validations
    if (this.currentOutcomes.length === 0) {
      this.noOutcomesSended = true;
      return;
    }

    if (this.editedCompetency.description.trim() === '') {
      return;
    }

    // 1. Update the competency first
    this.competencyService
      .updateCompetency(this.competency.id, this.editedCompetency)
      .subscribe({
        next: (updatedCompetency) => {
          // 2. Process outcomes after competency is updated
          this.processOutcomes();
        },
        error: (error) => {
          // Handle competency update error
        },
      });
  }

  /**
   * Handles cancel button click event
   */
  onCancelClick(): void {
    this.editStateChange.emit(false);
  }

  /**
   * Navigates to outcome detail page
   * @param outcome The outcome to navigate to
   * @param index The index of the outcome in the list
   */
  goToOutcome(outcome: SubjectOutcome, index: number): void {
    // Only navigate to outcomes with positive ID (saved in DB)
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
        console.log('Navigating to outcome:', outcome.id);
      })
      .catch((error) => {
        console.error('Navigation error:', error);
      });
  }

  // PRIVATE METHODS - BUSINESS LOGIC

  /**
   * Processes results from the reuse modal
   * @param result Array of outcomes selected in the modal
   */
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

    this.outcomes$ = of(this.currentOutcomes); // Update the observable
  }

  /**
   * Main method for processing outcomes
   * Determines what outcomes to add and remove, and handles constraints
   */
  private processOutcomes(): void {
    // Determine which outcomes to add and remove
    const outcomesToAdd = this.currentOutcomes.filter(
      (currentOutcome) =>
        !this.originalOutcomes.some(
          (originalOutcome) => originalOutcome.id === currentOutcome.id
        )
    );

    const outcomesToRemove = this.originalOutcomes.filter(
      (originalOutcome) =>
        !this.currentOutcomes.some(
          (currentOutcome) => currentOutcome.id === originalOutcome.id
        )
    );

    console.log('=== STARTING OUTCOMES PROCESS ===');
    console.log('Original outcomes:', this.originalOutcomes);
    console.log('Current outcomes:', this.currentOutcomes);
    console.log('Outcomes to add:', outcomesToAdd);
    console.log('Outcomes to remove:', outcomesToRemove);

    // Explicitly limit outcomes to add to not exceed the maximum
    let adjustedOutcomesToAdd = [...outcomesToAdd];
    let adjustedOutcomesToRemove = [...outcomesToRemove];

    // Calculate how many outcomes we'll have after operations
    const currentCountInDB = this.originalOutcomes.length;
    const projectedCount = currentCountInDB + adjustedOutcomesToAdd.length - adjustedOutcomesToRemove.length;

    console.log('Current count in DB:', currentCountInDB);
    console.log('Projected count after operations:', projectedCount);

    // Adjust if exceeds maximum
    if (projectedCount > this.maxOutcomes) {
      const maxToAdd = this.maxOutcomes - (currentCountInDB - adjustedOutcomesToRemove.length);
      adjustedOutcomesToAdd = adjustedOutcomesToAdd.slice(0, Math.max(0, maxToAdd));
      console.log('Adjusted for exceeding maximum. New outcomes to add:', adjustedOutcomesToAdd);
    }

    // Adjust if falls below minimum
    if (projectedCount < 1) {
      const maxToRemove = Math.max(0, currentCountInDB - 1);
      adjustedOutcomesToRemove = adjustedOutcomesToRemove.slice(0, maxToRemove);
      console.log('Adjusted for falling below minimum. New outcomes to remove:', adjustedOutcomesToRemove);
    }

    // Determine if we're in a special case and process intelligently
    this.processIntelligently(adjustedOutcomesToAdd, adjustedOutcomesToRemove, currentCountInDB);
  }

  /**
   * Process outcomes intelligently based on current state
   * @param outcomesToAdd Outcomes to be added
   * @param outcomesToRemove Outcomes to be removed
   * @param currentCountInDB Current count of outcomes in the database
   */
  private processIntelligently(
    outcomesToAdd: SubjectOutcome[],
    outcomesToRemove: SubjectOutcome[],
    currentCountInDB: number
  ): void {
    console.log('=== STARTING INTELLIGENT PROCESS ===');

    // If there are no operations, simply exit edit mode
    if (outcomesToAdd.length === 0 && outcomesToRemove.length === 0) {
      console.log('No operations to perform. Exiting edit mode.');
      this.exitEditMode();
      return;
    }

    // Special case 1: We're at maximum (3) and want to replace (first remove)
    if (currentCountInDB >= this.maxOutcomes && outcomesToAdd.length > 0) {
      console.log('SPECIAL CASE 1: At maximum and want to add. First remove, then add.');
      this.processRemovalsThenAdditions(outcomesToRemove, outcomesToAdd, currentCountInDB);
      return;
    }

    // Special case 2: We're at minimum (1) and want to replace (first add)
    if (currentCountInDB <= 1 && outcomesToRemove.length > 0) {
      console.log('SPECIAL CASE 2: At minimum and want to remove. First add, then remove.');
      this.processAdditionsThenRemovals(outcomesToAdd, outcomesToRemove, currentCountInDB);
      return;
    }

    // Normal case: We're at an intermediate point, we can do operations
    // in the safest order (first add, then remove)
    console.log('NORMAL CASE: At intermediate point. First add, then remove.');
    this.processAdditionsThenRemovals(outcomesToAdd, outcomesToRemove, currentCountInDB);
  }

  /**
   * Process removals first, then additions
   * Used when we're at maximum outcomes and need to make space
   * @param outcomesToRemove Outcomes to be removed
   * @param outcomesToAdd Outcomes to be added
   * @param currentDBCount Current count in database
   */
  private processRemovalsThenAdditions(
    outcomesToRemove: SubjectOutcome[],
    outcomesToAdd: SubjectOutcome[],
    currentDBCount: number
  ): void {
    console.log('=== STARTING REMOVE THEN ADD ===');
    console.log('Current count:', currentDBCount);
    console.log('Outcomes to remove:', outcomesToRemove);
    console.log('Outcomes to add:', outcomesToAdd);

    // If there's nothing to remove, go directly to additions
    if (outcomesToRemove.length === 0) {
      console.log('No outcomes to remove, proceeding directly to additions');
      this.processAllAdditions(outcomesToAdd, 0, currentDBCount);
      return;
    }

    const removalObservables = outcomesToRemove.map(outcome =>
      this.outcomeService.deleteOutcome(outcome.id)
    );

    console.log('Sending requests to remove', outcomesToRemove.length, 'outcomes');

    // Process removals in parallel
    forkJoin(removalObservables).subscribe({
      next: () => {
        // Update counter and proceed with additions
        const newCount = currentDBCount - outcomesToRemove.length;
        console.log('Removals completed successfully. New count:', newCount);
        this.processAllAdditions(outcomesToAdd, 0, newCount);
      },
      error: (error) => {
        // In case of error, try to proceed with additions anyway
        console.error('Error in removals:', error);
        console.log('Trying to proceed with additions anyway');
        const newCount = currentDBCount; // Assume nothing was removed
        this.processAllAdditions(outcomesToAdd, 0, newCount);
      }
    });
  }

  /**
   * Process additions first, then removals
   * Used when we're at minimum outcomes and need to add before removing
   * @param outcomesToAdd Outcomes to be added
   * @param outcomesToRemove Outcomes to be removed
   * @param currentDBCount Current count in database
   */
  private processAdditionsThenRemovals(
    outcomesToAdd: SubjectOutcome[],
    outcomesToRemove: SubjectOutcome[],
    currentDBCount: number
  ): void {
    console.log('=== STARTING ADD THEN REMOVE ===');
    console.log('Current count:', currentDBCount);
    console.log('Outcomes to add:', outcomesToAdd);
    console.log('Outcomes to remove:', outcomesToRemove);

    // If there's nothing to add, go directly to removals
    if (outcomesToAdd.length === 0) {
      console.log('No outcomes to add, proceeding directly to removals');
      this.processAllRemovals(outcomesToRemove, 0, currentDBCount);
      return;
    }

    // Process additions sequentially to maintain control
    console.log('Starting sequential addition processing');
    this.processAllAdditions(outcomesToAdd, 0, currentDBCount, outcomesToRemove);
  }

  /**
   * Process all additions sequentially
   * @param outcomesToAdd Outcomes to add
   * @param currentIndex Current index in the array
   * @param currentDBCount Current count in database
   * @param outcomesToRemove Optional outcomes to remove after additions
   */
  private processAllAdditions(
    outcomesToAdd: SubjectOutcome[],
    currentIndex: number,
    currentDBCount: number,
    outcomesToRemove: SubjectOutcome[] = []
  ): void {
    console.log('=== PROCESSING ADDITION ===');
    console.log('Current index:', currentIndex, 'of', outcomesToAdd.length);
    console.log('Current count in DB:', currentDBCount);
    console.log('Maximum allowed:', this.maxOutcomes);

    // If we've finished all additions or reached the maximum allowed
    if (currentIndex >= outcomesToAdd.length || currentDBCount >= this.maxOutcomes) {
      console.log('Additions completed or maximum reached');

      // Move to removals if there are any pending
      if (outcomesToRemove && outcomesToRemove.length > 0) {
        console.log('Proceeding to process pending removals');
        this.processAllRemovals(outcomesToRemove, 0, currentDBCount);
      } else {
        console.log('No pending removals, ending process');
        this.exitEditMode();
      }
      return;
    }

    const outcomeToAdd = outcomesToAdd[currentIndex];
    console.log('Adding outcome:', outcomeToAdd);

    // Add the current outcome
    this.outcomeService.createOutcome(
      this.subjectId,
      this.competency.id,
      outcomeToAdd
    ).subscribe({
      next: (result) => {
        // Increment counter and continue with next
        currentDBCount++;
        console.log('Addition successful. New count:', currentDBCount);
        console.log('Continuing with next addition...');
        this.processAllAdditions(outcomesToAdd, currentIndex + 1, currentDBCount, outcomesToRemove);
      },
      error: (error) => {
        // In case of error, move to next
        console.error('Error adding outcome:', error);
        console.log('Moving to next outcome without incrementing counter');
        this.processAllAdditions(outcomesToAdd, currentIndex + 1, currentDBCount, outcomesToRemove);
      }
    });
  }

  /**
   * Process all removals sequentially
   * @param outcomesToRemove Outcomes to remove
   * @param currentIndex Current index in the array
   * @param currentDBCount Current count in database
   */
  private processAllRemovals(
    outcomesToRemove: SubjectOutcome[],
    currentIndex: number,
    currentDBCount: number
  ): void {
    console.log('=== PROCESSING REMOVAL ===');
    console.log('Current index:', currentIndex, 'of', outcomesToRemove.length);
    console.log('Current count in DB:', currentDBCount);
    console.log('Minimum allowed:', 1);

    // If we've finished all removals or would go below minimum
    if (currentIndex >= outcomesToRemove.length || currentDBCount <= 1) {
      console.log('Removals completed or would go below minimum');
      console.log('Finishing edit process');
      this.exitEditMode();
      return;
    }

    const outcomeToRemove = outcomesToRemove[currentIndex];
    console.log('Removing outcome:', outcomeToRemove);

    // Remove the current outcome
    this.outcomeService.deleteOutcome(outcomeToRemove.id).subscribe({
      next: (result) => {
        // Decrement counter and continue with next
        currentDBCount--;
        console.log('Removal successful. New count:', currentDBCount);
        console.log('Continuing with next removal...');
        this.processAllRemovals(outcomesToRemove, currentIndex + 1, currentDBCount);
      },
      error: (error) => {
        // In case of error, move to next
        console.error('Error removing outcome:', error);
        console.log('Moving to next removal without decrementing counter');
        this.processAllRemovals(outcomesToRemove, currentIndex + 1, currentDBCount);
      }
    });
  }

  /**
   * Exit edit mode and notify parent component
   */
  private exitEditMode(): void {
    console.log('=== EXITING EDIT MODE ===');
    this.editStateChange.emit(false);
  }
}

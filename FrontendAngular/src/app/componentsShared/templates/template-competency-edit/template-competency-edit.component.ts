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
import { ModalConfirmComponent } from '../../messages/modal-confirm/modal-confirm.component';


interface ExtendedSubjectOutcome extends SubjectOutcome {
  isReused?: boolean;
  originalTermId?: number;
}


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
  @Output() saveSuccess = new EventEmitter<void>();
  // Competency data
  editedCompetency: SubjectCompetency = {} as SubjectCompetency;
  programCompetency$!: Observable<ProgramCompetency>;
  validationError: string = '';

  // Outcomes management
  originalOutcomes: ExtendedSubjectOutcome[] = []; // Immutable list - original state
  currentOutcomes: ExtendedSubjectOutcome[] = []; // Working list - what's shown in UI
  outcomes$!: Observable<ExtendedSubjectOutcome[]>; // Observable for the UI

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
  removeOutcome(outcome: ExtendedSubjectOutcome): void {
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
        // Marcar como nuevo
        const newOutcome: ExtendedSubjectOutcome = {
          ...result,
          isReused: false,
          id: -(this.currentOutcomes.length + 1) // ID temporal negativo
        };
        this.currentOutcomes.push(newOutcome);
        this.outcomes$ = of(this.currentOutcomes);
      }
    });
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
          this.saveSuccess.emit();
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
  goToOutcome(outcome: ExtendedSubjectOutcome, index: number): void {
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
    // Marcar outcomes reutilizados
    const serviceOutcomes = result.filter((outcome) => outcome.id > 0)
      .map(outcome => ({
        ...outcome,
        isReused: true,
        originalTermId: 1 // Guardar término original como 1 por defecto
      } as ExtendedSubjectOutcome));

    // Mantener outcomes creados (ya marcados como nuevos)
    const createdOutcomesInSelection = this.currentOutcomes.filter(
      (outcome) => outcome.id <= 0
    ).map(outcome => ({
      ...outcome,
      isReused: false
    } as ExtendedSubjectOutcome));

    this.currentOutcomes = [...serviceOutcomes, ...createdOutcomesInSelection];
    this.outcomes$ = of(this.currentOutcomes);
  }

  /**
   * Process all removals sequentially
   * @param outcomesToRemove Outcomes to remove
   * @param currentIndex Current index in the array
   * @param currentDBCount Current count in database
   */
  private processAllRemovals(
    outcomesToRemove: ExtendedSubjectOutcome[],
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

  // Separar entre reutilizados y nuevos
  const reuseOutcomes = outcomesToAdd.filter(outcome => outcome.isReused === true);
  const newOutcomes = outcomesToAdd.filter(outcome => outcome.isReused === false || outcome.isReused === undefined);

  console.log('Outcomes reutilizados:', reuseOutcomes);
  console.log('Outcomes nuevos:', newOutcomes);

  // NO ajustar los outcomes aquí - dejar que processIntelligentlyWithTypes maneje las restricciones
  const currentCountInDB = this.originalOutcomes.length;
  
  console.log('Current count in DB:', currentCountInDB);
  console.log('Total outcomes to add:', outcomesToAdd.length);
  console.log('Total outcomes to remove:', outcomesToRemove.length);

  // Pasar los outcomes sin ajustes previos
  this.processIntelligentlyWithTypes(
    reuseOutcomes, 
    newOutcomes, 
    outcomesToRemove, 
    currentCountInDB
  );
}
/**
 * Process outcomes intelligently with differentiation between reused and new outcomes
 * @param reuseOutcomes Outcomes to be reused from other terms
 * @param newOutcomes Outcomes to be created new
 * @param outcomesToRemove Outcomes to be removed
 * @param currentCountInDB Current count of outcomes in the database
 */
private processIntelligentlyWithTypes(
  reuseOutcomes: ExtendedSubjectOutcome[],
  newOutcomes: ExtendedSubjectOutcome[],
  outcomesToRemove: ExtendedSubjectOutcome[],
  currentCountInDB: number
): void {
  console.log('=== STARTING INTELLIGENT PROCESS WITH TYPES ===');

  const totalOutcomesToAdd = reuseOutcomes.length + newOutcomes.length;
  const finalCount = currentCountInDB - outcomesToRemove.length + totalOutcomesToAdd;
  const wouldGoBelowMinimum = (currentCountInDB - outcomesToRemove.length) < 1;

  console.log(`Current: ${currentCountInDB}, Removing: ${outcomesToRemove.length}, Adding: ${totalOutcomesToAdd}`);
  console.log(`Final projected count: ${finalCount}`);

  // If there are no operations, simply exit edit mode
  if (totalOutcomesToAdd === 0 && outcomesToRemove.length === 0) {
    console.log('No operations to perform. Exiting edit mode.');
    this.exitEditMode();
    return;
  }

  // Special case 1: Backend limitation - would temporarily exceed maximum during additions
  // This happens when currentCount + additions > max, even if we have removals pending
  const wouldExceedDuringAdditions = (currentCountInDB + totalOutcomesToAdd) > this.maxOutcomes;
  
  if (wouldExceedDuringAdditions && outcomesToRemove.length > 0) {
    console.log('SPECIAL CASE 1: Would exceed maximum during additions. Backend limitation - remove first.');
    console.log(`Current: ${currentCountInDB} + Adding: ${totalOutcomesToAdd} = ${currentCountInDB + totalOutcomesToAdd} > Max: ${this.maxOutcomes}`);
    console.log('Strategy: Remove first to make space, then add');
    this.processRemovalsThenAdditionsWithTypes(outcomesToRemove, reuseOutcomes, newOutcomes, currentCountInDB);
    return;
  }

  // Special case 2: At maximum and want to add more (but no removals to help)
  if (currentCountInDB >= this.maxOutcomes && totalOutcomesToAdd > 0 && outcomesToRemove.length === 0) {
    console.log('SPECIAL CASE 2: At maximum, want to add, but no removals available.');
    // Can't add anything, just exit
    this.exitEditMode();
    return;
  }

  // Special case 3: Would go below minimum during removal but have additions
  if (outcomesToRemove.length > 0 && wouldGoBelowMinimum && totalOutcomesToAdd > 0) {
    console.log('SPECIAL CASE 3: Would go below minimum during removal. First add, then remove.');
    
    // If we would exceed maximum temporarily, use remove-first strategy
    const wouldExceedMax = (currentCountInDB + totalOutcomesToAdd) > this.maxOutcomes;
    
    if (wouldExceedMax) {
      console.log('BACKEND LIMITATION: Would exceed max temporarily. Using remove-first strategy.');
      console.log('Strategy: Remove first to make space, then add');
      this.processRemovalsThenAdditionsWithTypes(outcomesToRemove, reuseOutcomes, newOutcomes, currentCountInDB);
      return;
    }
    
    // If we won't exceed maximum, use the original strategy (add first)
    this.processAdditionsThenRemovalsWithTypes(reuseOutcomes, newOutcomes, outcomesToRemove, currentCountInDB);
    return;
  }

  // Special case 3: Only removals and would go below minimum
  if (totalOutcomesToAdd === 0 && outcomesToRemove.length > 0 && wouldGoBelowMinimum) {
    console.log('SPECIAL CASE 3: Only removals that would go below minimum. Adjusting to keep minimum.');
    const maxRemovable = Math.max(0, currentCountInDB - 1);
    const adjustedOutcomesToRemove = outcomesToRemove.slice(0, maxRemovable);
    
    if (adjustedOutcomesToRemove.length > 0) {
      this.processAllRemovals(adjustedOutcomesToRemove, 0, currentCountInDB);
    } else {
      console.log('No removals possible without going below minimum. Exiting.');
      this.exitEditMode();
    }
    return;
  }

  // Special case 4: Final count would exceed maximum
  if (finalCount > this.maxOutcomes) {
    console.log('SPECIAL CASE 4: Final count would exceed maximum. Adjusting additions.');
    const maxToAdd = this.maxOutcomes - (currentCountInDB - outcomesToRemove.length);
    const totalAdjusted = Math.max(0, maxToAdd);
    
    // Priorizar reutilizados sobre nuevos si hay que limitar
    const adjustedReuse = reuseOutcomes.slice(0, Math.min(reuseOutcomes.length, totalAdjusted));
    const remainingSpace = totalAdjusted - adjustedReuse.length;
    const adjustedNew = newOutcomes.slice(0, Math.max(0, remainingSpace));
    
    if (adjustedReuse.length > 0 || adjustedNew.length > 0) {
      this.processAdditionsThenRemovalsWithTypes(adjustedReuse, adjustedNew, outcomesToRemove, currentCountInDB);
    } else if (outcomesToRemove.length > 0) {
      this.processAllRemovals(outcomesToRemove, 0, currentCountInDB);
    } else {
      this.exitEditMode();
    }
    return;
  }

  // Normal case: Safe to do operations in preferred order
  console.log('NORMAL CASE: Safe operation order - first add, then remove.');
  
  if (totalOutcomesToAdd > 0) {
    this.processAdditionsThenRemovalsWithTypes(reuseOutcomes, newOutcomes, outcomesToRemove, currentCountInDB);
  } else if (outcomesToRemove.length > 0) {
    this.processAllRemovals(outcomesToRemove, 0, currentCountInDB);
  } else {
    this.exitEditMode();
  }
}


/**
 * Process additions first, then removals with type differentiation
 */
private processAdditionsThenRemovalsWithTypes(
  reuseOutcomes: ExtendedSubjectOutcome[],
  newOutcomes: ExtendedSubjectOutcome[],
  outcomesToRemove: ExtendedSubjectOutcome[],
  currentDBCount: number
): void {
  console.log('=== STARTING ADD THEN REMOVE WITH TYPES ===');
  console.log('Current count:', currentDBCount);
  console.log('Reuse outcomes to add:', reuseOutcomes);
  console.log('New outcomes to add:', newOutcomes);
  console.log('Outcomes to remove:', outcomesToRemove);

  // If there's nothing to add, go directly to removals
  if (reuseOutcomes.length === 0 && newOutcomes.length === 0) {
    console.log('No outcomes to add, proceeding directly to removals');
    this.processAllRemovals(outcomesToRemove, 0, currentDBCount);
    return;
  }

  // Process additions sequentially to maintain control
  // Permitir exceder el máximo temporalmente porque tenemos removals pendientes
  const allowExceedMax = outcomesToRemove.length > 0;
  console.log('Starting sequential addition processing with types, allowExceedMax:', allowExceedMax);
  this.processAllAdditionsWithTypes(reuseOutcomes, newOutcomes, 0, currentDBCount, outcomesToRemove, allowExceedMax);
}

/**
 * Process removals first, then additions with type differentiation
 */
private processRemovalsThenAdditionsWithTypes(
  outcomesToRemove: ExtendedSubjectOutcome[],
  reuseOutcomes: ExtendedSubjectOutcome[],
  newOutcomes: ExtendedSubjectOutcome[],
  currentDBCount: number
): void {
  console.log('=== STARTING REMOVE THEN ADD WITH TYPES ===');
  console.log('Current count:', currentDBCount);
  console.log('Outcomes to remove:', outcomesToRemove);
  console.log('Reuse outcomes to add:', reuseOutcomes);
  console.log('New outcomes to add:', newOutcomes);

  // If there's nothing to remove, go directly to additions
  if (outcomesToRemove.length === 0) {
    console.log('No outcomes to remove, proceeding directly to additions');
    this.processAllAdditionsWithTypes(reuseOutcomes, newOutcomes, 0, currentDBCount, [], false); // No allow exceed aquí
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
      this.processAllAdditionsWithTypes(reuseOutcomes, newOutcomes, 0, newCount, [], false); // No allow exceed después de remover
    },
    error: (error) => {
      // In case of error, try to proceed with additions anyway
      console.error('Error in removals:', error);
      console.log('Trying to proceed with additions anyway');
      const newCount = currentDBCount; // Assume nothing was removed
      this.processAllAdditionsWithTypes(reuseOutcomes, newOutcomes, 0, newCount, [], false);
    }
  });
}
/**
 * Process all additions sequentially with type differentiation
 * First process reuse outcomes, then new outcomes
 */
private processAllAdditionsWithTypes(
  reuseOutcomes: ExtendedSubjectOutcome[],
  newOutcomes: ExtendedSubjectOutcome[],
  currentPhase: number, // 0: reuse phase, 1: new phase
  currentDBCount: number,
  outcomesToRemove: ExtendedSubjectOutcome[] = [],
  allowExceedMax: boolean = false // Nuevo parámetro para permitir exceder el máximo
): void {
  console.log('=== PROCESSING ADDITIONS WITH TYPES ===');
  console.log('Current phase:', currentPhase === 0 ? 'REUSE' : 'NEW');
  console.log('Current count in DB:', currentDBCount);
  console.log('Maximum allowed:', this.maxOutcomes);
  console.log('Allow exceed max:', allowExceedMax);
  console.log('Reuse outcomes count:', reuseOutcomes.length);
  console.log('New outcomes count:', newOutcomes.length);
  console.log('Outcomes to remove pending:', outcomesToRemove.length);

  // Phase 0: Process reuse outcomes
  if (currentPhase === 0) {
    if (reuseOutcomes.length === 0) {
      console.log('No reuse outcomes, moving to new outcomes phase');
      this.processAllAdditionsWithTypes(reuseOutcomes, newOutcomes, 1, currentDBCount, outcomesToRemove, allowExceedMax);
      return;
    }

    console.log('Processing reuse outcomes in parallel');
    
    // Process reuse outcomes - usar el método específico para reutilizar
    const reuseObservables = reuseOutcomes.map(outcome => {
      // Usar el método específico para reutilizar outcomes
      return this.outcomeService.copyOutcome(outcome.id, this.competency.id, this.subjectId);
    });

    forkJoin(reuseObservables).subscribe({
      next: () => {
        const newCount = currentDBCount + reuseOutcomes.length;
        console.log('Reuse outcomes completed successfully. New count:', newCount);
        console.log('Moving to new outcomes phase');
        this.processAllAdditionsWithTypes(reuseOutcomes, newOutcomes, 1, newCount, outcomesToRemove, allowExceedMax);
      },
      error: (error) => {
        console.error('Error in reuse outcomes:', error);
        console.log('Moving to new outcomes phase anyway');
        this.processAllAdditionsWithTypes(reuseOutcomes, newOutcomes, 1, currentDBCount, outcomesToRemove, allowExceedMax);
      }
    });
    return;
  }

  // Phase 1: Process new outcomes sequentially
  // Solo detener por máximo si NO se permite exceder Y no hay removals pendientes
  const shouldStopForMax = !allowExceedMax && currentDBCount >= this.maxOutcomes;
  
  if (newOutcomes.length === 0 || shouldStopForMax) {
    console.log('New outcomes phase completed or maximum reached (and not allowed to exceed)');

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

  console.log('Processing new outcomes sequentially');
  this.processNewOutcomesSequentially(newOutcomes, 0, currentDBCount, outcomesToRemove, allowExceedMax);
}

/**
 * Process new outcomes sequentially (one by one)
 */
private processNewOutcomesSequentially(
  newOutcomes: ExtendedSubjectOutcome[],
  currentIndex: number,
  currentDBCount: number,
  outcomesToRemove: ExtendedSubjectOutcome[] = [],
  allowExceedMax: boolean = false // Nuevo parámetro
): void {
  console.log('=== PROCESSING NEW OUTCOME ===');
  console.log('Current index:', currentIndex, 'of', newOutcomes.length);
  console.log('Current count in DB:', currentDBCount);
  console.log('Allow exceed max:', allowExceedMax);
  console.log('Max outcomes allowed:', this.maxOutcomes);
  console.log('Outcomes to remove pending:', outcomesToRemove.length);

  // Condición corregida: Solo detener por máximo si NO se permite exceder
  // allowExceedMax será true cuando haya eliminaciones pendientes
  const shouldStopForMax = !allowExceedMax && currentDBCount >= this.maxOutcomes;
  
  console.log('Should stop for max?', shouldStopForMax, '(!allowExceedMax:', !allowExceedMax, '&& currentDBCount >= maxOutcomes:', currentDBCount >= this.maxOutcomes, ')');

  // If we've finished all new outcomes or reached the maximum allowed (and not allowed to exceed)
  if (currentIndex >= newOutcomes.length || shouldStopForMax) {
    console.log('New outcomes completed or maximum reached (and not allowed to exceed)');

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

  const outcomeToAdd = newOutcomes[currentIndex];
  console.log('Creating new outcome:', outcomeToAdd);

  // Create the new outcome
  this.outcomeService.createOutcome(
    this.subjectId,
    this.competency.id,
    outcomeToAdd
  ).subscribe({
    next: (result) => {
      // Increment counter and continue with next
      currentDBCount++;
      console.log('New outcome creation successful. New count:', currentDBCount);
      console.log('Continuing with next new outcome...');
      this.processNewOutcomesSequentially(newOutcomes, currentIndex + 1, currentDBCount, outcomesToRemove, allowExceedMax);
    },
    error: (error) => {
      // In case of error, move to next
      console.error('Error creating new outcome:', error);
      console.log('Moving to next outcome without incrementing counter');
      this.processNewOutcomesSequentially(newOutcomes, currentIndex + 1, currentDBCount, outcomesToRemove, allowExceedMax);
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

import {
  Component,
  EventEmitter,
  Input,
  Output,
  OnChanges,
  SimpleChanges,
} from '@angular/core';
import { MoleculeBlockUserComponent } from '../../molecules/molecule-block-user/molecule-block-user.component';
import { TeacherDTO } from '../../../models/TeacherDTO';
import { CommonModule } from '@angular/common';
import { BehaviorSubject, forkJoin, of } from 'rxjs';
import { EvaluatorAssignmentService } from '../../../services/evaluator_assignment.service';

@Component({
  selector: 'app-template-remove-evaluator',
  imports: [CommonModule, MoleculeBlockUserComponent],
  templateUrl: './template-remove-evaluator-component.component.html',
  styleUrl: './template-remove-evaluator-component.component.css',
})
export class TemplateRemoveEvaluatorAssignment implements OnChanges {
  @Input() selectedEvaluators: TeacherDTO[] = [];
  @Input() originalEvaluatorsUID: string[] = [];
  @Input() subjectOutcomeId: number = -1;
  @Output() evaluatorRemoved = new EventEmitter<TeacherDTO>();
  @Output() saveCompleted = new EventEmitter<boolean>();

  isSaving: boolean = false;
  saveError: string | null = null;
  saveSuccess: boolean = false;

  private evaluatorsSubject = new BehaviorSubject<TeacherDTO[]>([]);
  evaluators$ = this.evaluatorsSubject.asObservable();

  constructor(private evaluatorAssignmentService: EvaluatorAssignmentService) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['selectedEvaluators']) {
      this.evaluatorsSubject.next([...(this.selectedEvaluators || [])]);
    }
  }

  removeEvaluator(evaluator: TeacherDTO): void {
    this.evaluatorRemoved.emit(evaluator);
  }

  /**
   * Guarda los cambios en las asignaciones de evaluadores:
   * - Elimina las asignaciones de evaluadores que ya no están seleccionados
   * - Agrega asignaciones para los nuevos evaluadores seleccionados
   */
  saveAssignedEvaluators(): void {
    if (this.subjectOutcomeId <= 0) {
      this.saveError = 'ID de resultado de asignatura no válido.';
      return;
    }

    this.isSaving = true;
    this.saveError = null;
    this.saveSuccess = false;
    // 1. Obtener los IDs de los evaluadores actualmente seleccionados y convertirlos a string
    const currentSelectedUIDs = this.selectedEvaluators
      .map((evaluator) => String(evaluator.id || ''))
      .filter((id) => id !== '');

    // 2. Identificar evaluadores a eliminar (están en originales pero no en seleccionados)
    const evaluatorsToRemove = this.originalEvaluatorsUID.filter(
      (uid) => !currentSelectedUIDs.includes(String(uid))
    );

    // 3. Identificar evaluadores a añadir (están en seleccionados pero no en originales)
    const evaluatorsToAdd = currentSelectedUIDs.filter(
      (uid) => !this.originalEvaluatorsUID.map(String).includes(uid)
    );

    console.log('Evaluadores originales:', this.originalEvaluatorsUID);
    console.log('Evaluadores actuales:', currentSelectedUIDs);
    console.log('Evaluadores a eliminar:', evaluatorsToRemove);
    console.log('Evaluadores a añadir:', evaluatorsToAdd);

    // 4. Preparar operaciones para eliminar asignaciones
    const removeOperations = evaluatorsToRemove.map((evaluatorUID) =>
      this.evaluatorAssignmentService.deleteAssignmentByevaluatorAndSubjectOutcome(
        evaluatorUID,
        this.subjectOutcomeId
      )
    );

    // 5. Preparar operaciones para añadir asignaciones
    const addOperations = evaluatorsToAdd.map((evaluatorUID) =>
      this.evaluatorAssignmentService.assignevaluatorToSubjectOutcomeInActiveTerm(
        evaluatorUID,
        this.subjectOutcomeId
      )
    );

    // 6. Combinar todas las operaciones y ejecutarlas en paralelo
    const allOperations = [...removeOperations, ...addOperations];

    // Si no hay operaciones que realizar, mostrar éxito
    if (allOperations.length === 0) {
      this.saveSuccess = true;
      this.isSaving = false;
      this.saveCompleted.emit(true);
      return;
    }

    // Ejecutar todas las operaciones
    forkJoin(allOperations).subscribe({
      next: (results) => {
        console.log('Resultados de operaciones:', results);
        this.saveSuccess = true;
        this.isSaving = false;
        this.saveCompleted.emit(true);
      },
      error: (error) => {
        console.error('Error al guardar asignaciones:', error);
        this.saveError =
          'Error al guardar los cambios. Por favor, inténtelo de nuevo.';
        this.isSaving = false;
        this.saveCompleted.emit(false);
      },
    });
  }

  getFullName(evaluator: TeacherDTO): string {
    return `${evaluator.name} ${evaluator.lastName}`;
  }
}
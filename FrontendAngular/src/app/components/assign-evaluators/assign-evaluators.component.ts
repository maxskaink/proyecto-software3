import { EvaluatorAssignment } from '../../models/EvaluatorAssignmentDTO';
import {
  Component,
  OnInit,
} from '@angular/core';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecules/molecule-back-header/molecule-back-header.component';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { LoadingComponent } from '../../componentsShared/loading/loading.component';
import { TeacherDTO } from '../../models/TeacherDTO';

import { AuthService } from '../../services/auth.service';
import { BehaviorSubject, forkJoin } from 'rxjs';
import { TemplateSearchChooseEvaluatorsComponent } from '../../componentsShared/templates/template-search-choose-evaluators.component /template-search-choose-teachers.component/template-search-choose-evaluators.component.component';
import { EvaluatorAssignmentService } from '../../services/evaluator_assignment.service';
import { TemplateRemoveEvaluatorAssignment } from "../../componentsShared/templates/template-remove-evaluator-component/template-remove-evaluator-component.component";

@Component({
  selector: 'app-assign-evaluators',
  imports: [
    CommonModule,
    LoadingComponent,
    MoleculeBackHeaderComponent,
    TemplateSearchChooseEvaluatorsComponent,
    TemplateRemoveEvaluatorAssignment
],
  templateUrl: './assign-evaluators.component.html',
  styleUrl: './assign-evaluators.component.css',
})
export class AssignEvaluatorsComponent implements OnInit {
  isLoading: boolean = false;
  subjectOutcomeId: number = -1;
  teachers: TeacherDTO[] = [];
  originalEvaluatorsUID: string[] = [];
  originalAssignments: EvaluatorAssignment[] = [];

  // Usamos BehaviorSubject para manejar las actualizaciones en tiempo real
  private selectedEvaluatorsSubject = new BehaviorSubject<TeacherDTO[]>([]);
  selectedEvaluators$ = this.selectedEvaluatorsSubject.asObservable();

  // Mantenemos una propiedad para acceder fácilmente al valor actual
  get selectedEvaluators(): TeacherDTO[] {
    return this.selectedEvaluatorsSubject.getValue();
  }

  constructor(
    private route: ActivatedRoute, 
    private auth: AuthService, 
    private evaluatorService: EvaluatorAssignmentService
  ) {}

  ngOnInit() {
    this.isLoading = true;
    // Obtener el ID del outcome de los parámetros de consulta (query params)
    this.route.queryParams.subscribe(params => {
      const outcomeId = params['outcomeId'];
      
      if (!outcomeId) {
        console.error("The subject outcome doesn't exist");
        this.isLoading = false;
        return;
      }

      this.subjectOutcomeId = Number(outcomeId);
      this.loadInitialData();
    });
  }

  loadInitialData(): void {
    this.isLoading = true;

    // Cargar los profesores y las asignaciones en paralelo
    forkJoin({
      teachers: this.auth.getAllUsers(),
      assignments: this.evaluatorService.getAssignmentsBySubjectOutcome(this.subjectOutcomeId)
    }).subscribe({
      next: (result) => {
        this.teachers = result.teachers;
        this.originalAssignments = result.assignments;
        this.originalEvaluatorsUID = result.assignments.map(a => a.evaluatorUid);

        // Filtrar los profesores que ya están asignados
        const selectedEvaluators = this.teachers.filter(evaluator =>
          this.originalEvaluatorsUID.includes(evaluator.id || '')
        );

        // Actualizar la lista de profesores seleccionados
        this.selectedEvaluatorsSubject.next(selectedEvaluators);

        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading data:', err);
        this.isLoading = false;
      }
    });
  }

  // Método para manejar la selección de evaluadores
  onEvaluatorSelectionChange(event: { evaluator: TeacherDTO; isSelected: boolean }): void {
    const currentSelected = this.selectedEvaluatorsSubject.getValue();

    if (event.isSelected) {
      // Si el evaluador se seleccionó, añadirlo a la lista si no está ya
      if (!this.isEvaluatorSelected(event.evaluator)) {
        const updatedList = [...currentSelected, event.evaluator];
        this.selectedEvaluatorsSubject.next(updatedList);
      }
    } else {
      // Si el evaluador se deseleccionó, quitarlo de la lista
      const updatedList = currentSelected.filter(
        e => e.identification !== event.evaluator.identification
      );
      this.selectedEvaluatorsSubject.next(updatedList);
    }
  }

  // Método para manejar la eliminación de evaluadores
  onEvaluatorRemoved(evaluator: TeacherDTO): void {
    const currentSelected = this.selectedEvaluatorsSubject.getValue();

    // Quitar el evaluador de la lista de seleccionados
    const updatedList = currentSelected.filter(
      e => e.identification !== evaluator.identification
    );

    this.selectedEvaluatorsSubject.next(updatedList);
  }

  // Método auxiliar para verificar si un evaluador ya está seleccionado
  private isEvaluatorSelected(evaluator: TeacherDTO): boolean {
    return this.selectedEvaluatorsSubject.getValue().some(
      e => e.identification === evaluator.identification
    );
  }

  getSubjectOutcomeId(): number {
    return this.subjectOutcomeId;
  }
}
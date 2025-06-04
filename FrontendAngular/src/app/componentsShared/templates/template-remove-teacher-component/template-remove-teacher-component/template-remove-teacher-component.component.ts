import {
  Component,
  EventEmitter,
  Input,
  Output,
  OnChanges,
  SimpleChanges,
} from '@angular/core';
import { MoleculeBlockUserComponent } from '../../../molecules/molecule-block-user/molecule-block-user.component';
import { TeacherDTO } from '../../../../models/TeacherDTO';
import { CommonModule } from '@angular/common';
import { BehaviorSubject, forkJoin, of } from 'rxjs';
import { TeacherAssignmentService } from '../../../../services/teacher_assignment.service';

@Component({
  selector: 'app-template-remove-teacher',
  imports: [CommonModule, MoleculeBlockUserComponent],
  templateUrl: './template-remove-teacher-component.component.html',
  styleUrl: './template-remove-teacher-component.component.css',
})
export class TemplateRemoveTeacherComponentComponent implements OnChanges {
  @Input() selectedTeachers: TeacherDTO[] = [];
  @Input() originalTeachersUID: string[] = [];
  @Input() subjectId: number = -1;
  @Output() teacherRemoved = new EventEmitter<TeacherDTO>();
  @Output() saveCompleted = new EventEmitter<boolean>();

  isSaving: boolean = false;
  saveError: string | null = null;
  saveSuccess: boolean = false;

  private teachersSubject = new BehaviorSubject<TeacherDTO[]>([]);
  teachers$ = this.teachersSubject.asObservable();

  constructor(private teacherAssignmentService: TeacherAssignmentService) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['selectedTeachers']) {
      this.teachersSubject.next([...(this.selectedTeachers || [])]);
    }
  }

  removeTeacher(teacher: TeacherDTO): void {
    this.teacherRemoved.emit(teacher);
  }

  /**
   * Guarda los cambios en las asignaciones de profesores:
   * - Elimina las asignaciones de profesores que ya no están seleccionados
   * - Agrega asignaciones para los nuevos profesores seleccionados
   */
  saveAssignedTeachers(): void {
    if (this.subjectId <= 0) {
      this.saveError = 'ID de asignatura no válido.';
      return;
    }

    this.isSaving = true;
    this.saveError = null;
    this.saveSuccess = false;
    // 1. Obtener los IDs de los profesores actualmente seleccionados y convertirlos a string
    const currentSelectedUIDs = this.selectedTeachers
      .map((teacher) => String(teacher.id || ''))
      .filter((id) => id !== '');

    // 2. Identificar profesores a eliminar (están en originales pero no en seleccionados)
    const teachersToRemove = this.originalTeachersUID.filter(
      (uid) => !currentSelectedUIDs.includes(String(uid))
    );

    // 3. Identificar profesores a añadir (están en seleccionados pero no en originales)
    const teachersToAdd = currentSelectedUIDs.filter(
      (uid) => !this.originalTeachersUID.map(String).includes(uid)
    );

    console.log('Profesores originales:', this.originalTeachersUID);
    console.log('Profesores actuales:', currentSelectedUIDs);
    console.log('Profesores a eliminar:', teachersToRemove);
    console.log('Profesores a añadir:', teachersToAdd);

    // 4. Preparar operaciones para eliminar asignaciones
    const removeOperations = teachersToRemove.map((teacherUID) =>
      this.teacherAssignmentService.deleteAssignmentByTeacherAndSubject(
        teacherUID,
        this.subjectId
      )
    );

    // 5. Preparar operaciones para añadir asignaciones
    const addOperations = teachersToAdd.map((teacherUID) =>
      this.teacherAssignmentService.assignTeacherToSubjectInActiveTerm(
        teacherUID,
        this.subjectId
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

  getFullName(teacher: TeacherDTO): string {
    return `${teacher.name} ${teacher.lastName}`;
  }
}

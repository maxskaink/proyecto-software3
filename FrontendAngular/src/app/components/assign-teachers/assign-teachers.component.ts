import {
  Component,
  OnInit,
} from '@angular/core';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecules/molecule-back-header/molecule-back-header.component';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { LoadingComponent } from '../../componentsShared/loading/loading.component';
import { TeacherDTO } from '../../models/TeacherDTO';
import { TemplateRemoveTeacherComponentComponent } from '../../componentsShared/templates/template-remove-teacher-component/template-remove-teacher-component/template-remove-teacher-component.component';
import { AuthService } from '../../services/auth.service';
import { BehaviorSubject } from 'rxjs';
import { TemplateSearchChooseTeachersComponentComponent } from "../../componentsShared/templates/template-search-choose-teachers.component/template-search-choose-teachers.component/template-search-choose-teachers.component.component";

@Component({
  selector: 'app-assign-teachers',
  imports: [
    CommonModule,
    LoadingComponent,
    MoleculeBackHeaderComponent,
    TemplateRemoveTeacherComponentComponent,
    TemplateSearchChooseTeachersComponentComponent
],
  templateUrl: './assign-teachers.component.html',
  styleUrl: './assign-teachers.component.css',
})
export class AssignTeachersComponent implements OnInit {
  isLoading: boolean = false;
  subjectId: number = -1;
  teachers: TeacherDTO[] = [];

  // Usamos BehaviorSubject para manejar las actualizaciones en tiempo real
  private selectedTeachersSubject = new BehaviorSubject<TeacherDTO[]>([]);
  selectedTeachers$ = this.selectedTeachersSubject.asObservable();
Selecciona: any;

  // Mantenemos una propiedad para acceder fácilmente al valor actual
  get selectedTeachers(): TeacherDTO[] {
    return this.selectedTeachersSubject.getValue();
  }

  constructor(private route: ActivatedRoute, private auth: AuthService) {}

  ngOnInit() {
    this.isLoading = true;
    const response = this.route.snapshot.paramMap.get('id');
    if (!response) {
      console.error("The subject doesn't exist");
      this.isLoading = false;
      return;
    }

    this.subjectId = Number(response);
    this.loadInitialData();
  }

  loadInitialData(): void {
    this.auth.getAllUsers().subscribe({
      next: (teachers: TeacherDTO[]) => {
        this.teachers = teachers;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading teachers:', err);
        this.isLoading = false;
      },
    });
  }

  // Método para manejar la selección de profesores desde template-choose-teachers
  onTeacherSelectionChange(event: { teacher: TeacherDTO; isSelected: boolean }): void {
    const currentSelected = this.selectedTeachersSubject.getValue();

    if (event.isSelected) {
      // Si el profesor se seleccionó, añadirlo a la lista si no está ya
      if (!this.isTeacherSelected(event.teacher)) {
        const updatedList = [...currentSelected, event.teacher];
        this.selectedTeachersSubject.next(updatedList);
      }
    } else {
      // Si el profesor se deseleccionó, quitarlo de la lista
      const updatedList = currentSelected.filter(
        t => t.identification !== event.teacher.identification
      );
      this.selectedTeachersSubject.next(updatedList);
    }
  }

  // Método para manejar la eliminación de profesores desde template-remove-teacher
  onTeacherRemoved(teacher: TeacherDTO): void {
    const currentSelected = this.selectedTeachersSubject.getValue();

    // Quitar el profesor de la lista de seleccionados
    const updatedList = currentSelected.filter(
      t => t.identification !== teacher.identification
    );

    this.selectedTeachersSubject.next(updatedList);
  }

  // Método auxiliar para verificar si un profesor ya está seleccionado
  private isTeacherSelected(teacher: TeacherDTO): boolean {
    return this.selectedTeachersSubject.getValue().some(
      t => t.identification === teacher.identification
    );
  }


}

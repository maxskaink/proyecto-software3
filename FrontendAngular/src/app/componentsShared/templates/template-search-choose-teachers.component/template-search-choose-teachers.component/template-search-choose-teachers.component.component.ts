import { Component, EventEmitter, Input, Output, OnChanges, SimpleChanges } from '@angular/core';
import { MoleculeBlockUserComponent } from '../../../molecules/molecule-block-user/molecule-block-user.component';
import { TeacherDTO } from '../../../../models/TeacherDTO';
import { CommonModule } from '@angular/common';
import { BehaviorSubject, Observable, combineLatest, map, startWith } from 'rxjs';
import { FormsModule, ReactiveFormsModule, FormControl } from '@angular/forms';

// Interface for teachers with selection state
interface SelectableTeacher extends TeacherDTO {
  selected: boolean;
  disabled?: boolean;
}

@Component({
  selector: 'app-template-search-choose-teachers',
  imports: [
    CommonModule,
    MoleculeBlockUserComponent,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './template-search-choose-teachers.component.component.html',
  styleUrl: './template-search-choose-teachers.component.component.css'
})
export class TemplateSearchChooseTeachersComponentComponent implements OnChanges {
  @Input() listTeachers: TeacherDTO[] = [];
  @Input() selectedTeachers: TeacherDTO[] = [];
  @Input() maxTeachers?: number = 3;
  @Output() teacherSelectionChange = new EventEmitter<{
    teacher: TeacherDTO;
    isSelected: boolean;
  }>();

  // Control para la búsqueda
  searchControl = new FormControl('');

  // BehaviorSubject para los profesores
  private teachersSubject = new BehaviorSubject<SelectableTeacher[]>([]);

  teachers$: Observable<SelectableTeacher[]>;

  selectedCount: number = 0;
  allSelectedTeachers: TeacherDTO[] = [];

  constructor() {
    // Combinar la lista de profesores con el término de búsqueda
    this.teachers$ = combineLatest([
      this.teachersSubject,
      this.searchControl.valueChanges.pipe(
        // Aseguramos que empieza con cadena vacía para mostrar todos al inicio
        startWith(''),
        // Manejar valores nulos o indefinidos
        map(term => term || '')
      )
    ]).pipe(
      map(([teachers, searchTerm]) => {
        // Si la búsqueda está vacía, retornar todos los profesores
        if (!searchTerm || !searchTerm.trim()) {
          return teachers;
        }

        // Filtrar basado en el término de búsqueda
        const term = searchTerm.toLowerCase();
        return teachers.filter(teacher =>
          (teacher.name || '').toLowerCase().includes(term) ||
          (teacher.role || '').toLowerCase().includes(term) ||
          (teacher.identification?.toString() || '').includes(term)
        );
      })
    );
  }


  ngOnInit(): void {
    // Asegurar que los datos se inicialicen al cargar el componente
    this.initializeData();

    // Forzar la renderización inicial con un valor vacío
    // Lo hacemos en ngOnInit para asegurar que ocurra después de la detección de cambios inicial
    setTimeout(() => {
      this.searchControl.setValue('');
    }, 0);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['listTeachers'] || changes['selectedTeachers']) {
      this.initializeData();
    }
  }


  private initializeData(): void {
    this.allSelectedTeachers = [...this.selectedTeachers];
    this.selectedCount = this.allSelectedTeachers.length;
    this.updateTeachersObservable();
  }

  private updateTeachersObservable(): void {
    const selectableTeachers: SelectableTeacher[] = this.listTeachers.map(teacher => ({
      ...teacher,
      selected: this.isTeacherInList(teacher, this.allSelectedTeachers),
      disabled: this.selectedCount >= (this.maxTeachers || 3) && !this.isTeacherInList(teacher, this.allSelectedTeachers)
    }));

    this.teachersSubject.next(selectableTeachers);
  }


  private isTeacherInList(teacher: TeacherDTO, list: TeacherDTO[]): boolean {
    return list.some(t => t.identification === teacher.identification);
  }


  isTeacherSelected(teacher: TeacherDTO): boolean {
    return this.isTeacherInList(teacher, this.allSelectedTeachers);
  }

  toggleTeacherSelection(teacher: SelectableTeacher): void {
    if (teacher.selected) {
      this.processSelectionToggle(teacher);
      return;
    }

    if (this.selectedCount >= (this.maxTeachers || 3)) {
      return;
    }

    this.processSelectionToggle(teacher);
  }

  private processSelectionToggle(teacher: SelectableTeacher): void {
    const currentTeachers = this.teachersSubject.getValue();

    const updatedTeachers = currentTeachers.map(t => {
      if (t.identification === teacher.identification) {
        const newSelected = !t.selected;

        if (newSelected) {
          if (!this.isTeacherInList(t, this.allSelectedTeachers)) {
            this.allSelectedTeachers.push(t);
          }
        } else {
          this.allSelectedTeachers = this.allSelectedTeachers.filter(
            st => st.identification !== t.identification
          );
        }

        this.selectedCount = this.allSelectedTeachers.length;

        this.teacherSelectionChange.emit({
          teacher: t,
          isSelected: newSelected
        });

        return { ...t, selected: newSelected };
      }
      return t;
    });

    const finalTeachers = updatedTeachers.map(t => ({
      ...t,
      disabled: this.selectedCount >= (this.maxTeachers || 3) && !t.selected
    }));

    this.teachersSubject.next(finalTeachers);
  }

  clearSearch(): void {
    this.searchControl.setValue('');
  }
}

import { Component, EventEmitter, Input, Output, OnChanges, SimpleChanges } from '@angular/core';
import { MoleculeBlockUserComponent } from '../../../molecules/molecule-block-user/molecule-block-user.component';
import { TeacherDTO } from '../../../../models/TeacherDTO';
import { CommonModule } from '@angular/common';
import { BehaviorSubject, Observable, combineLatest, map, startWith } from 'rxjs';
import { FormsModule, ReactiveFormsModule, FormControl } from '@angular/forms';

// Interface for evaluators with selection state
interface SelectableEvaluator extends TeacherDTO {
  selected: boolean;
  disabled?: boolean;
}

@Component({
  selector: 'app-template-search-choose-evaluators',
  imports: [
    CommonModule,
    MoleculeBlockUserComponent,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './template-search-choose-evaluators.component.component.html',
  styleUrl: './template-search-choose-evaluators.component.component.css'
})
export class TemplateSearchChooseEvaluatorsComponent implements OnChanges {
  @Input() listEvaluators: TeacherDTO[] = [];
  @Input() selectedEvaluators: TeacherDTO[] = [];
  @Input() maxEvaluators?: number = 3;
  @Output() evaluatorSelectionChange = new EventEmitter<{
    evaluator: TeacherDTO;
    isSelected: boolean;
  }>();

  // Control para la búsqueda
  searchControl = new FormControl('');

  // BehaviorSubject para los profesores
  private evaluatorOutcome = new BehaviorSubject<SelectableEvaluator[]>([]);

  evaluators$: Observable<SelectableEvaluator[]>;

  selectedCount: number = 0;
  allSelectedEvaluators: TeacherDTO[] = [];

  constructor() {
    // Combinar la lista de profesores con el término de búsqueda
    this.evaluators$ = combineLatest([
      this.evaluatorOutcome,
      this.searchControl.valueChanges.pipe(
        // Aseguramos que empieza con cadena vacía para mostrar todos al inicio
        startWith(''),
        // Manejar valores nulos o indefinidos
        map(term => term || '')
      )
    ]).pipe(
      map(([evaluators, searchTerm]) => {
        // Si la búsqueda está vacía, retornar todos los profesores
        if (!searchTerm || !searchTerm.trim()) {
          return evaluators;
        }

        // Filtrar basado en el término de búsqueda
        const term = searchTerm.toLowerCase();
        return evaluators.filter(evaluator =>
          (evaluator.name || '').toLowerCase().includes(term) ||
          (evaluator.role || '').toLowerCase().includes(term) ||
          (evaluator.identification?.toString() || '').includes(term)
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
    if (changes['listEvaluators'] || changes['selectedEvaluators']) {
      this.initializeData();
    }
  }


  private initializeData(): void {
    this.allSelectedEvaluators = [...this.selectedEvaluators];
    this.selectedCount = this.allSelectedEvaluators.length;
    this.updateTeachersObservable();
  }

  private updateTeachersObservable(): void {
    const selectableTeachers: SelectableEvaluator[] = this.listEvaluators.map(evaluator => ({
      ...evaluator,
      selected: this.isTeacherInList(evaluator, this.allSelectedEvaluators),
      disabled: this.selectedCount >= (this.maxEvaluators || 3) && !this.isTeacherInList(evaluator, this.allSelectedEvaluators)
    }));

    this.evaluatorOutcome.next(selectableTeachers);
  }


  private isTeacherInList(evaluator: TeacherDTO, list: TeacherDTO[]): boolean {
    return list.some(t => t.identification === evaluator.identification);
  }


  isTeacherSelected(evaluator: TeacherDTO): boolean {
    return this.isTeacherInList(evaluator, this.allSelectedEvaluators);
  }

  toggleTeacherSelection(evaluator: SelectableEvaluator): void {
    if (evaluator.selected) {
      this.processSelectionToggle(evaluator);
      return;
    }

    if (this.selectedCount >= (this.maxEvaluators || 3)) {
      return;
    }

    this.processSelectionToggle(evaluator);
  }

  private processSelectionToggle(evaluator: SelectableEvaluator): void {
    const currentTeachers = this.evaluatorOutcome.getValue();

    const updatedTeachers = currentTeachers.map(t => {
      if (t.identification === evaluator.identification) {
        const newSelected = !t.selected;

        if (newSelected) {
          if (!this.isTeacherInList(t, this.allSelectedEvaluators)) {
            this.allSelectedEvaluators.push(t);
          }
        } else {
          this.allSelectedEvaluators = this.allSelectedEvaluators.filter(
            st => st.identification !== t.identification
          );
        }

        this.selectedCount = this.allSelectedEvaluators.length;

        this.evaluatorSelectionChange.emit({
          evaluator: t,
          isSelected: newSelected
        });

        return { ...t, selected: newSelected };
      }
      return t;
    });

    const finalTeachers = updatedTeachers.map(t => ({
      ...t,
      disabled: this.selectedCount >= (this.maxEvaluators || 3) && !t.selected
    }));

    this.evaluatorOutcome.next(finalTeachers);
  }

  clearSearch(): void {
    this.searchControl.setValue('');
  }

  getFullName(evaluator: TeacherDTO): string {
    return `${evaluator.name} ${evaluator.lastName}`;
  }
}

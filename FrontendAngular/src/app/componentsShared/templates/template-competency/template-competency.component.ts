import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SubjectCompetency } from '../../../models/SubjectCompetencyDTO';
import { SubjectOutcome } from '../../../models/SubjectOutcomeDTO';
import { catchError, Observable, of, tap } from 'rxjs';
import { ProgramCompetency } from '../../../models/ProgramCompetencyDTO';
import { SubjectOutomeService } from '../../../services/subject_outcome.service';
import { ProgramCompetencyService } from '../../../services/program-competency.service';
import { CommonModule } from '@angular/common';
import { MoleculeOutComeComponent } from '../../molecules/molecule-out-come/molecule-out-come.component';
import { CompetencyComponent } from '../../../components/competency/competency.component';

@Component({
  selector: 'app-template-competency',
  imports: [CommonModule, MoleculeOutComeComponent],
  templateUrl: './template-competency.component.html',
  styleUrl: './template-competency.component.css'
})
export class TemplateCompetencyComponent {
  @Input() competency!: SubjectCompetency;
  @Output() editStateChange = new EventEmitter<boolean>();
  outcomes$!: Observable<SubjectOutcome[]>;
  programCompetency$!: Observable<ProgramCompetency>;
  
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
    private competencyProgramService: ProgramCompetencyService
  ) {}

  ngOnInit(): void {
    if (this.competency) {
      this.loadOutcomes();
      this.loadProgramCompetency();
    }
  }

  loadOutcomes(): void {
    this.loading.outcomes = true;
    this.outcomes$ = this.outcomeService.getOutcomesByCompetency(this.competency.id).pipe(
      tap(() => this.loading.outcomes = false),
      catchError(error => {
        console.error('Error loading outcomes:', error);
        this.error.outcomes = true;
        this.loading.outcomes = false;
        return of([]);
      })
    );
  }

  loadProgramCompetency(): void {
    this.loading.programCompetency = true;
    this.programCompetency$ = this.competencyProgramService.getById(this.competency.programCompetencyId).pipe(
      tap(() => this.loading.programCompetency = false),
      catchError(error => {
        console.error('Error loading program competency:', error);
        this.error.programCompetency = true;
        this.loading.programCompetency = false;
        return of({} as ProgramCompetency);
      })
    );
  }
  onSaveClick(): void {
    // Aquí iría la lógica para guardar los cambios
    this.editStateChange.emit(false);
  }

  onCancelClick(): void {
    this.editStateChange.emit(false);
  }
  onEditClick():void{
    this.editStateChange.emit(true);
  }
}

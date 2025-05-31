import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SubjectCompetency } from '../../../models/SubjectCompetencyDTO';
import { SubjectOutcome } from '../../../models/SubjectOutcomeDTO';
import { catchError, Observable, of, tap } from 'rxjs';
import { ProgramCompetency } from '../../../models/ProgramCompetencyDTO';
import { SubjectOutomeService } from '../../../services/subject_outcome.service';
import { ProgramCompetencyService } from '../../../services/program-competency.service';
import { CommonModule } from '@angular/common';
import { SubjectCompetencyService } from '../../../services/subject_competency.service';

@Component({
  selector: 'app-template-competency',
  imports: [CommonModule],
  templateUrl: './template-competency.component.html',
  styleUrl: './template-competency.component.css'
})
export class TemplateCompetencyComponent {
  @Input() competency!: SubjectCompetency;
  @Output() editStateChange = new EventEmitter<boolean>();
  outcomes$!: Observable<SubjectOutcome[]>;
  programCompetency$!: Observable<ProgramCompetency>;
  editedCompetency: SubjectCompetency = {} as SubjectCompetency;
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
    private competencyService: SubjectCompetencyService
  ) {}

  ngOnInit(): void {
    if (this.competency) {
      this.loadOutcomes();
      this.loadProgramCompetency();
      this.editedCompetency = { ...this.competency };
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

  onDeletelClick(): void {
    this.competencyService.deleteCompetency(this.competency.id).subscribe({
      next: () => { 
        console.log('Competency deleted successfully');
        this.loadProgramCompetency();
        this.editStateChange.emit(false);
      }
      , error: (error) => {
        console.error('Error deleting competency:', error);
      }
    });

  }
  onEditClick():void{
    this.editStateChange.emit(true);
  }
}

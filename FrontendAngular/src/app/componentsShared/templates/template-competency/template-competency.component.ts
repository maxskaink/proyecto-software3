import { Component, EventEmitter, Inject, Input, Output, PLATFORM_ID } from '@angular/core';
import { SubjectCompetency } from '../../../models/SubjectCompetencyDTO';
import { SubjectOutcome } from '../../../models/SubjectOutcomeDTO';
import { catchError, Observable, of, tap } from 'rxjs';
import { ProgramCompetency } from '../../../models/ProgramCompetencyDTO';
import { SubjectOutomeService } from '../../../services/subject_outcome.service';
import { ProgramCompetencyService } from '../../../services/program-competency.service';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { SubjectCompetencyService } from '../../../services/subject_competency.service';
import { MoleculeOutComeComponent } from '../../molecules/molecule-out-come/molecule-out-come.component';
import { EditStateService } from '../../../services/edit-state.service';

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
    @Inject(PLATFORM_ID) platformId: Object,
    private outcomeService: SubjectOutomeService, 
    private competencyProgramService: ProgramCompetencyService,
    private editStateService: EditStateService,
    private competencyService: SubjectCompetencyService
  ) {  }

  ngOnInit(): void {
    if (this.competency) {
      this.loadProgramCompetency;
      this.loadOutcomes();
      this.editedCompetency = { ...this.competency };
      console.log('Competency loaded:', this.competency);
      console.log(this.outcomes$);
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

  onEditClick(): void {
    this.editStateService.setEditState(true);
    this.editStateChange.emit(true);
  }
  onDeletelClick(): void{

  }


}

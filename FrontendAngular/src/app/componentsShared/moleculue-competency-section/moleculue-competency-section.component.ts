import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { MoleculeOutComeComponent } from '../molecule-out-come/molecule-out-come.component';

import { SubjectOutomeService } from '../../services/subject_outcome.service';
import { ProgramCompetencyService } from '../../services/program-competency.service';
import { SubjectCompetency } from '../../models/SubjectCompetencyDTO';
import { SubjectOutcome } from '../../models/SubjectOutcomeDTO';
import { catchError, Observable, of, tap } from 'rxjs';
import { ProgramCompetency } from '../../models/ProgramCompetencyDTO';

@Component({
    selector: 'app-moleculue-competency-section',
    standalone: true,
    imports: [CommonModule, MoleculeOutComeComponent],
    templateUrl: './moleculue-competency-section.component.html',
    styleUrl: './moleculue-competency-section.component.css'
})
export class MoleculeCompetencySectionComponent implements OnInit {
  @Input() competency!: SubjectCompetency;
  
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
}
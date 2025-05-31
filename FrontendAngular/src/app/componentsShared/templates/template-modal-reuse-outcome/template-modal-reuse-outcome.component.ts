import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { catchError, Observable, of, tap, map } from 'rxjs';

import { MoleculeOutComeComponent } from '../../molecules/molecule-out-come/molecule-out-come.component';
import { SubjectOutcome } from '../../../models/SubjectOutcomeDTO';
import { SubjectOutomeService } from '../../../services/subject_outcome.service';

// Interface for outcomes with selection state
interface SelectableOutcome extends SubjectOutcome {
  selected: boolean;
}

@Component({
  selector: 'app-template-modal-reuse-outcome',
  standalone: true,
  imports: [
    CommonModule, 
    MatDialogModule, 
    MatIconModule,
    FormsModule, 
    MoleculeOutComeComponent
  ],
  templateUrl: './template-modal-reuse-outcome.component.html',
  styleUrl: './template-modal-reuse-outcome.component.css'
})
export class TemplateModalReuseOutcomeComponent implements OnInit {
  selectedOption: string = '';
  outcomes$!: Observable<SelectableOutcome[]>;
  loading = {
    outcomes: false
  };
  
  error = {
    outcomes: false
  };

  constructor(
    public dialogRef: MatDialogRef<TemplateModalReuseOutcomeComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { 
      subjectId: number, 
      options: string[], 
      selectedOutcomes: SubjectOutcome[],
      selectDescription?: string, 
      selectPlaceholder?: string 
    },
    private outcomeService: SubjectOutomeService
  ) { }

  ngOnInit() {
    if (this.data?.options?.length > 0) {
      this.selectedOption = this.data.options[0];
      this.onOptionChange();
    }
  }

  existSelectDescription(): boolean {
    return !!this.data.selectDescription && this.data.selectDescription.trim() !== '';
  }

  loadOutcomes(): void {
    this.loading.outcomes = true;
    this.outcomes$ = this.outcomeService.getOutcomesBySubject(this.data.subjectId).pipe(
      map(outcomes => outcomes.map(outcome => ({
        ...outcome,
        selected: this.data.selectedOutcomes?.some(
          selected => selected.id === outcome.id
        ) || false
      }))),
      tap(() => this.loading.outcomes = false),
      catchError(error => {
        this.error.outcomes = true;
        this.loading.outcomes = false;
        return of([]);
      })
    );
  }

  onOptionChange(): void {
    if (this.selectedOption) {
      this.loadOutcomes();
    }
  }

  toggleOutcomeSelection(outcome: SelectableOutcome): void {
    this.outcomes$.subscribe(outcomes => {
      const updatedOutcomes = outcomes.map(o =>
        o.id === outcome.id ? { ...o, selected: !o.selected } : o
      );
      this.outcomes$ = of(updatedOutcomes);
    });
  }
  
  getSelectedOutcomes(): Observable<SubjectOutcome[]> {
    return this.outcomes$.pipe(
      map(outcomes => outcomes.filter(o => o.selected))
    );
  }

  saveSelectedOutcomes(): void {
    this.getSelectedOutcomes().subscribe(selectedOutcomes => {
      if (selectedOutcomes.length > 0) {
        this.dialogRef.close(selectedOutcomes);
      }
    });
  }
}
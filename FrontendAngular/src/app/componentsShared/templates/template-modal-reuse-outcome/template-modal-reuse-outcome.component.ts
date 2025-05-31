import { CommonModule } from '@angular/common';
import { Component, Inject, Input, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { MoleculeOutComeComponent } from '../../molecules/molecule-out-come/molecule-out-come.component';
import { catchError, Observable, of, tap, map } from 'rxjs';
import { SubjectOutcome } from '../../../models/SubjectOutcomeDTO';
import { SubjectOutomeService } from '../../../services/subject_outcome.service';

// Add this interface
interface SelectableOutcome extends SubjectOutcome {
  selected: boolean;
}

@Component({
  selector: 'app-template-modal-reuse-outcome',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatIconModule,
    FormsModule, MoleculeOutComeComponent],
  templateUrl: './template-modal-reuse-outcome.component.html',
  styleUrl: './template-modal-reuse-outcome.component.css'
})
export class TemplateModalReuseOutcomeComponent implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<TemplateModalReuseOutcomeComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { 
      subjectId: number, 
      options: string[], 
      selectDescription?: string, 
      selectPlaceholder?: string 
    },
    private outcomeService: SubjectOutomeService
  ) { }

  selectedOption: string = '';
  outcomes$!: Observable<SelectableOutcome[]>; // Changed type
  loading = {
    outcomes: false,
    programCompetency: false
  };

  error = {
    outcomes: false,
    programCompetency: false
  };

  ngOnInit() {
    // After data is loaded
    if (this.data && this.data.options && this.data.options.length > 0) {
      // Set the first option as selected by default
      this.selectedOption = this.data.options[0];

      // Trigger any logic that depends on selection
      this.onOptionChange();
    }
  }

  existSelectDescription(): boolean {
    return !!this.data.selectDescription && this.data.selectDescription.trim() !== '';
  }

  onSelectBlur() {
    // This method can be used to handle any logic when the select loses focus
  }

  loadOutcomes(): void {
    this.loading.outcomes = true;
    this.outcomes$ = this.outcomeService.getOutcomesBySubject(this.data.subjectId).pipe(
      // Transform each SubjectOutcome into a SelectableOutcome
      map(outcomes => outcomes.map(outcome => ({
        ...outcome,
        selected: false // Initialize as not selected
      }))),
      tap(() => this.loading.outcomes = false),
      catchError(error => {
        console.error('Error loading outcomes:', error);
        this.error.outcomes = true;
        this.loading.outcomes = false;
        return of([]);
      })
    );
  }

  closeDialog(): void {
    // Get only the selected outcomes when closing
    this.outcomes$.subscribe(outcomes => {
      const selectedOutcomes = outcomes.filter(o => o.selected);
      this.dialogRef.close(selectedOutcomes);
    });
  }

  onOptionChange(): void {
    // Handle option change, like loading outcomes for the selected option
    if (this.selectedOption) {
      // Load outcomes based on the selected option
      this.loadOutcomes();
    }
  }

  toggleOutcomeSelection(outcome: SelectableOutcome): void {
    outcome.selected = !outcome.selected; // Toggle selection state
  }
  
  // Add method to get selected outcomes
  getSelectedOutcomes(): Observable<SubjectOutcome[]> {
    return this.outcomes$.pipe(
      map(outcomes => outcomes.filter(o => o.selected))
    );
  }
}
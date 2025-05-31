import { CommonModule } from '@angular/common';
import { Component, Inject, Input } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { MoleculeOutComeComponent } from '../../molecules/molecule-out-come/molecule-out-come.component';
import { catchError, Observable, of, tap } from 'rxjs';
import { SubjectOutcome } from '../../../models/SubjectOutcomeDTO';
import { SubjectOutomeService } from '../../../services/subject_outcome.service';

@Component({
  // Removed invalid imports property
  imports: [CommonModule, MatDialogModule, MatIconModule,
    FormsModule, MoleculeOutComeComponent],
  templateUrl: './template-modal-reuse-outcome.component.html',
  styleUrl: './template-modal-reuse-outcome.component.css'
})
export class TemplateModalReuseOutcomeComponent {
  constructor(
    public dialogRef: MatDialogRef<TemplateModalReuseOutcomeComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { subjectId: number, options: string[], selectDescription?: string, selectPlaceholder?: string },
    private outcomeService: SubjectOutomeService
  ) { }

  selectedOption: string = '';
  outcomes$!: Observable<SubjectOutcome[]>;
  loading = {
    outcomes: false,
    programCompetency: false
  };

  error = {
    outcomes: false,
    programCompetency: false
  };

  // In template-modal-reuse-outcome.component.ts
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
    this.dialogRef.close(this.selectedOption); // Devuelve la opción seleccionada al cerrar el modal
  }

  onOptionChange(): void {
    // Handle option change, like loading outcomes for the selected option
    if (this.selectedOption) {
      // Load outcomes based on the selected option
      this.loadOutcomes();
    }
  }
}


import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { catchError, Observable, of, tap, map } from 'rxjs';

import { MoleculeOutComeComponent } from '../../molecules/molecule-out-come/molecule-out-come.component';
import { SubjectOutcome } from '../../../models/SubjectOutcomeDTO';
import { SubjectOutomeService } from '../../../services/subject_outcome.service';
import { TermService} from '../../../services/term.service';
import { TermDTO } from '../../../models/TermDTO';

// Interface for outcomes with selection state
interface SelectableOutcome extends SubjectOutcome {
  selected: boolean;
  isDuplicate?: boolean; // Marks outcomes with duplicate descriptions
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
  selectedCount: number = 0;
  terms: TermDTO[] = [];
  allSelectedOutcomes: SubjectOutcome[] = [];
  currentTermOutcomes: SelectableOutcome[] = []; // Stores outcomes for current term
  
  loading = {
    outcomes: false,
    terms: false
  };

  error = {
    outcomes: false,
    terms: false
  };

  constructor(
    public dialogRef: MatDialogRef<TemplateModalReuseOutcomeComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {
      subjectId: number,
      selectedOutcomes: SubjectOutcome[],
      maxOutcomes: number,
      selectDescription?: string,
      selectPlaceholder?: string
    },
    private outcomeService: SubjectOutomeService,
    private termService: TermService
  ) { 
    this.allSelectedOutcomes = [...data.selectedOutcomes];
  }

  ngOnInit() {
    this.loadTerms();
    // Initialize counter with created learning outcomes
    this.selectedCount = this.allSelectedOutcomes.length;
  }

  // Load available terms
  private loadTerms(): void {
    this.loading.terms = true;
    this.termService.getTerms().subscribe({
      next: (terms) => {
        this.terms = terms;
        this.loading.terms = false;
        
        // Select first term by default
        if (this.terms.length > 0) {
          this.selectedOption = this.formatTermOption(this.terms[0]);
          this.onTermChange();
        }
      },
      error: () => {
        this.error.terms = true;
        this.loading.terms = false;
      }
    });
  }

  // Format term option display text
  formatTermOption(term: TermDTO): string {
    return term.description;
  }

  // Get all term options for select dropdown
  getTermOptions(): string[] {
    return this.terms.map(term => this.formatTermOption(term));
  }

  // Get the ID of the selected term
  private getSelectedTermId(): number {
    const selectedTermDesc = this.selectedOption.trim();
    const selectedTerm = this.terms.find(term => 
      this.formatTermOption(term) === selectedTermDesc
    );
    return selectedTerm?.id || -1;
  }

  existSelectDescription(): boolean {
    return !!this.data.selectDescription && this.data.selectDescription.trim() !== '';
  }

  // Load outcomes for the selected term
  loadOutcomes(): void {
    const termId = this.getSelectedTermId();
    if (termId === -1) return;
    
    this.loading.outcomes = true;
    this.outcomes$ = this.outcomeService.getOutcomesBySubjectAndTerm(this.data.subjectId, termId).pipe(
      map(outcomes => outcomes.map(outcome => {
        // Check if already selected in global list
        const isSelected = this.allSelectedOutcomes.some(
          selected => selected.id === outcome.id
        );
        
        // Check if description already exists in selected outcomes
        const isDuplicate = this.allSelectedOutcomes.some(
          selected => 
            selected.id !== outcome.id && // Not the same outcome
            selected.description.trim().toLowerCase() === outcome.description.trim().toLowerCase() // Same description
        );
        
        return {
          ...outcome,
          selected: isSelected,
          isDuplicate: isDuplicate
        };
      })),
      tap(outcomes => {
        // Update total selected count
        this.selectedCount = this.allSelectedOutcomes.length;
        this.loading.outcomes = false;
        
        // Save reference to current term outcomes
        this.currentTermOutcomes = [...outcomes];
      }),
      catchError(() => {
        this.error.outcomes = true;
        this.loading.outcomes = false;
        return of([]);
      })
    );
  }

  // Handle term selection change
  onTermChange(): void {
    if (this.selectedOption) {
      // Save current selections before changing
      this.saveCurrentTermSelections();
      
      // Load outcomes for new term
      this.loadOutcomes();
    }
  }

  // Toggle outcome selection state
  toggleOutcomeSelection(outcome: SelectableOutcome): void {
    // If already selected, always allow deselection
    if (outcome.selected) {
      this.processSelectionToggle(outcome);
      return;
    }
    
    // Don't allow selection if duplicate or exceeds max limit
    if (outcome.isDuplicate || this.selectedCount >= (this.data.maxOutcomes || 3)) {
      return;
    }
    
    // Process selection if all checks pass
    this.processSelectionToggle(outcome);
  }

  // Process the toggle of outcome selection
  private processSelectionToggle(outcome: SelectableOutcome): void {
    this.outcomes$.subscribe(outcomes => {
      // Update outcome in current list
      let updatedOutcomes = outcomes.map(o => {
        if (o.id === outcome.id) {
          const newSelected = !o.selected;
          
          // Update global selected outcomes list
          if (newSelected) {
            if (!this.allSelectedOutcomes.some(so => so.id === o.id)) {
              this.allSelectedOutcomes.push(o);
            }
          } else {
            this.allSelectedOutcomes = this.allSelectedOutcomes.filter(so => so.id !== o.id);
          }
          
          // Update total counter
          this.selectedCount = this.allSelectedOutcomes.length;
          
          return { ...o, selected: newSelected };
        }
        return o;
      });
      
      // Recalculate duplicates based on global list
      updatedOutcomes = updatedOutcomes.map(o => {
        // Don't mark as duplicate if already selected
        if (o.selected) return o;
        
        // Check if it's a duplicate by comparing with global list
        const isDuplicate = this.allSelectedOutcomes.some(
          selected => 
            selected.id !== o.id && // Not the same outcome
            selected.description.trim().toLowerCase() === o.description.trim().toLowerCase() // Same description
        );
        
        return { ...o, isDuplicate: isDuplicate };
      });
      
      this.outcomes$ = of(updatedOutcomes);
      
      // Update reference to current outcomes
      this.currentTermOutcomes = [...updatedOutcomes];
    });
  }

  // Get all selected outcomes
  getSelectedOutcomes(): Observable<SubjectOutcome[]> {
    // Ensure current selections are saved before returning
    this.saveCurrentTermSelections();
    return of(this.allSelectedOutcomes);
  }

  // Save selected outcomes and close dialog
  saveSelectedOutcomes(): void {
    this.getSelectedOutcomes().subscribe(selectedOutcomes => {
      this.dialogRef.close(selectedOutcomes);
    });
  }

  // Save selections from current term to global list
  saveCurrentTermSelections(): void {
    if (this.outcomes$) {
      this.outcomes$.subscribe(outcomes => {
        // Update global list with current selections
        outcomes.forEach(outcome => {
          if (outcome.selected) {
            // Check if already exists in global list
            const existingIndex = this.allSelectedOutcomes.findIndex(
              o => o.id === outcome.id
            );
            
            if (existingIndex === -1) {
              // Add if not exists
              this.allSelectedOutcomes.push(outcome);
            }
          } else {
            // Remove if not selected
            this.allSelectedOutcomes = this.allSelectedOutcomes.filter(
              o => o.id !== outcome.id
            );
          }
        });
        
        // Save reference to current outcomes
        this.currentTermOutcomes = [...outcomes];
      });
    }
  }
}
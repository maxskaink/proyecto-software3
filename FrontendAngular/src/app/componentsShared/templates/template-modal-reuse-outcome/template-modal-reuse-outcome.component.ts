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
  selectedCount: number = 0;
  
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
      maxOutcomes: number,
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
    
    // Inicializar el contador con los outcomes ya seleccionados
    this.selectedCount = this.data.selectedOutcomes?.length || 0;
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
      tap(outcomes => {
        // Actualizar el contador al cargar los datos
        this.selectedCount = outcomes.filter(o => o.selected).length;
        this.loading.outcomes = false;
      }),
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
    // Si intenta seleccionar (no está seleccionado actualmente)
    if (!outcome.selected) {
      // Verificar si excede el máximo de outcomes permitidos
      if (this.selectedCount >= (this.data.maxOutcomes || 3)) {
        // No permitir más selecciones
        return;
      }
    }
    
    this.outcomes$.subscribe(outcomes => {
      const updatedOutcomes = outcomes.map(o => {
        if (o.id === outcome.id) {
          // Actualizar el conteo de seleccionados
          this.selectedCount = o.selected ? this.selectedCount - 1 : this.selectedCount + 1;
          return { ...o, selected: !o.selected };
        }
        return o;
      });
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
import { Component, EventEmitter, Inject, Input, Output, PLATFORM_ID } from '@angular/core';
import { SubjectCompetency } from '../../../models/SubjectCompetencyDTO';
import { SubjectOutcome } from '../../../models/SubjectOutcomeDTO';
import { catchError, Observable, of, tap } from 'rxjs';
import { ProgramCompetency } from '../../../models/ProgramCompetencyDTO';
import { SubjectOutomeService } from '../../../services/subject_outcome.service';
import { ProgramCompetencyService } from '../../../services/program-competency.service';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { MoleculeOutComeComponent } from '../../molecules/molecule-out-come/molecule-out-come.component';
import { SubjectCompetencyService } from '../../../services/subject_competency.service';
import { FormsModule } from '@angular/forms';
import { EditStateService } from '../../../services/edit-state.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-template-competency-edit',
  imports: [CommonModule, MoleculeOutComeComponent, FormsModule],
  templateUrl: './template-competency-edit.component.html',
  styleUrl: './template-competency-edit.component.css'
})
export class TemplateCompetencyEditComponent {
    @Input() competency!: SubjectCompetency;
    @Output() editStateChange = new EventEmitter<boolean>();

    
    editedCompetency: SubjectCompetency = {} as SubjectCompetency;
     
    outcomes$!: Observable<SubjectOutcome[]>;
    programCompetency$!: Observable<ProgramCompetency>;
    validationError: string = '';


    
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
      private editStateService: EditStateService,
      private competencyService: SubjectCompetencyService,
      private router: Router
    ) {
    }
  
    ngOnInit(): void {
      if (this.competency) {
        this.loadProgramCompetency();
        this.loadOutcomes();
        this.editedCompetency = {
          ...this.competency
        };
        console.log('Outomces loaded:', this.outcomes$);
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
    validateInputs(): boolean {
      if (!this.editedCompetency.description || this.editedCompetency.description.trim() === '') {
        this.validationError = 'La descripción de la competencia es obligatoria.';
        return false;
      }
      if (!this.editedCompetency.programCompetencyId) {
        this.validationError = 'Debe seleccionar una competencia de programa.';
        return false;
      }
      this.validationError = '';
      return true;
    }
    onSaveClick(): void {
      if (!this.validateInputs()) return;

      this.competencyService.updateCompetency(1, this.editedCompetency).subscribe({
        next: () => {
          this.editStateService.setEditState(false);
          this.editStateChange.emit(false);
          },
        error: (error) => {
          console.error('Error updating competency:', error);
        }
      });
    }
    
    onCancelClick(): void {
      this.editStateService.setEditState(false);
      this.editStateChange.emit(false);
    }
    onAddOutcome():void{

    }

    goToOutcome(outcome: SubjectOutcome, index: number): void {
      // Usar el ID real del outcome si está disponible
      const outcomeId = outcome.id;
      
      this.router.navigate(['/home/subject/competency/subject/outcome'], {
          queryParams: {
              outcomeId: outcomeId,
              // Agregar otros parámetros necesarios si los hay
          }
      }).then(() => {
          console.log('Navegando al outcome:', outcomeId);
      }).catch(error => {
          console.error('Error en la navegación:', error);
      });
    }
 

}

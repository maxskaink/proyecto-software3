import {
  Component,
  EventEmitter,
  Inject,
  Input,
  Output,
  PLATFORM_ID,
} from '@angular/core';
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
import { Router } from '@angular/router';

@Component({
  selector: 'app-template-competency',
  imports: [CommonModule, MoleculeOutComeComponent],
  templateUrl: './template-competency.component.html',
  styleUrl: './template-competency.component.css',
})
export class TemplateCompetencyComponent {
  @Input() competency!: SubjectCompetency;
  @Output() editStateChange = new EventEmitter<boolean>();
  outcomes$!: Observable<SubjectOutcome[]>;
  programCompetency$!: Observable<ProgramCompetency>;
  descriptionProgram: string = '';
  editedCompetency: SubjectCompetency = {} as SubjectCompetency;
  isButtonsVisible = false; // Propiedad para controlar la visibilidad de los botones
  private readonly isBrowser: boolean;
  private scrollListener: any;

  loading = {
    outcomes: false,
    programCompetency: false,
  };

  error = {
    outcomes: false,
    programCompetency: false,
  };

  constructor(
    @Inject(PLATFORM_ID) platformId: Object,
    private outcomeService: SubjectOutomeService,
    private competencyProgramService: ProgramCompetencyService,
    private editStateService: EditStateService,
    private router: Router
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
  }

  ngOnInit(): void {
    if (this.competency) {
      this.loadProgramCompetency();
      this.loadOutcomes();
      this.editedCompetency = { ...this.competency };
    }
  }

  ngAfterViewInit(): void {
    if (this.isBrowser) {
      this.setupButtonVisibilityObserver();
    }
  }

  ngOnDestroy(): void {
    // Limpiamos el listener de scroll cuando el componente se destruye
    if (this.isBrowser && this.scrollListener) {
      window.removeEventListener('scroll', this.scrollListener);
    }
  }

  private setupButtonVisibilityObserver(): void {
    if (!this.isBrowser) return;

    let scrollTimer: any = null;
    let hasPassedThreshold = false; // Variable para rastrear si ya pasamos el punto límite
    let lastScrollY = 0; // Para detectar dirección del scroll
    const offsetThreshold = 100; // Pixels antes del borde donde queremos que aparezcan los botones

    this.scrollListener = () => {
      if (scrollTimer) clearTimeout(scrollTimer);

      scrollTimer = setTimeout(() => {
        const competencyElement = document.getElementById(
          `competency-${this.competency.id}`
        );
        if (!competencyElement) return;

        const rect = competencyElement.getBoundingClientRect();
        const currentScrollY = window.scrollY;
        const scrollingDown = currentScrollY > lastScrollY;
        lastScrollY = currentScrollY;

        // Si la parte superior del componente se acerca al borde superior de la ventana
        if (rect.top <= offsetThreshold) {
          hasPassedThreshold = true;
          this.isButtonsVisible = true;
        }
        // Si estamos scrolleando hacia arriba y el componente está completamente por debajo del umbral
        else if (!scrollingDown) {
          hasPassedThreshold = false;
          this.isButtonsVisible = false;
        }
        // Si estamos scrolleando hacia abajo después de haber pasado el umbral
        else if (scrollingDown && hasPassedThreshold) {
          this.isButtonsVisible = true;
        } else {
          this.isButtonsVisible = false;
        }
      }, 50);
    };

    // Añadir el listener de scroll
    window.addEventListener('scroll', this.scrollListener);

    // Verificación inicial
    setTimeout(() => {
      const competencyElement = document.getElementById(
        `competency-${this.competency.id}`
      );
      if (competencyElement) {
        const rect = competencyElement.getBoundingClientRect();
        hasPassedThreshold = rect.top <= offsetThreshold;
        this.isButtonsVisible = hasPassedThreshold;
      }
    }, 500);
  }
  
  loadOutcomes(): void {
    this.loading.outcomes = true;
    this.outcomes$ = this.outcomeService
      .getOutcomesByCompetency(this.competency.id)
      .pipe(
        tap(() => (this.loading.outcomes = false)),
        catchError((error) => {
          console.error('Error loading outcomes:', error);
          this.error.outcomes = true;
          this.loading.outcomes = false;
          return of([]);
        })
      );
  }
  loadProgramCompetency(): void {
    this.loading.programCompetency = true;
    this.programCompetency$ = this.competencyProgramService
      .getById(this.competency.programCompetencyId)
      .pipe(
        tap(() => (this.loading.programCompetency = false)),
        catchError((error) => {
          console.error('Error loading program competency:', error);
          this.error.programCompetency = true;
          this.loading.programCompetency = false;
          this.descriptionProgram = 'No program competency found';
          return of({} as ProgramCompetency);
        })
      );
    this.programCompetency$
      .pipe(
        tap(
          (programCompetency) =>
            (this.descriptionProgram = programCompetency.description)
        )
      )
      .subscribe();
  }

  onEditClick(): void {
    this.editStateService.setEditState(true);
    this.editStateChange.emit(true);
  }
  onDeletelClick(): void {}
  goToOutcome(outcome: SubjectOutcome, index: number): void {
    // Usar el ID real del outcome si está disponible
    const outcomeId = outcome.id;

    this.router
      .navigate(['/home/subject/competency/subject/outcome'], {
        queryParams: {
          outcomeId: outcomeId,
          // Agregar otros parámetros necesarios si los hay
        },
      })
      .then(() => {
        console.log('Navegando al outcome:', outcomeId);
      })
      .catch((error) => {
        console.error('Error en la navegación:', error);
      });
  }
}

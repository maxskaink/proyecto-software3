import {
  Component,
  ElementRef,
  ViewChild,
  Inject,
  PLATFORM_ID,
} from '@angular/core';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecules/molecule-back-header/molecule-back-header.component';
import { ActivatedRoute, Router } from '@angular/router';
import { SubjectDTO } from '../../models/SubjectDTO';
import { SubjectService } from '../../services/subject.service';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { SubjectCompetencyService } from '../../services/subject_competency.service';
import { SubjectCompetency } from '../../models/SubjectCompetencyDTO';
import { TemplateCompetencyComponent } from '../../componentsShared/templates/template-competency/template-competency.component';
import { TemplateCompetencyEditComponent } from '../../componentsShared/templates/template-competency-edit/template-competency-edit.component';
import { AuthService } from '../../services/auth.service';
import { EditStateService } from '../../services/edit-state.service';
import { LoadingComponent } from '../../componentsShared/loading/loading.component';
import { TemplateListTeachersComponent } from '../../componentsShared/templates/template-list-teachers/template-list-teachers.component';
import { TeacherAssignmentService } from '../../services/teacher_assignment.service';
import { TeacherDTO } from '../../models/TeacherDTO';
import { AlertmessageComponent } from '../../componentsShared/messages/alertmessage/alertmessage.component';
import { ChangeDetectorRef } from '@angular/core';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';
declare var bootstrap: any;

@Component({
  selector: 'app-asignature',
  imports: [
    TemplateCompetencyComponent,
    TemplateCompetencyEditComponent,
    TemplateListTeachersComponent,
    CommonModule,
    LoadingComponent,
    AlertmessageComponent,TemplateHeaderTitleComponent
  ],
  templateUrl: './subject.component.html',
  styleUrl: './subject.component.css',
})
export class SubjectComponent {
  @ViewChild('competencyCarousel') carousel!: ElementRef;
  private carouselInstance: any;
  private readonly isBrowser: boolean;
  role: string | null = null;
  isLoading: boolean = false;
  description: string = 'description';
  title: string = 'title';
  actualAsignature: SubjectDTO | null = null;
  listCompetency: SubjectCompetency[] = [];
  id: number = -1;
  isCompetencySectionVisible = false;
  isEdit: boolean = false;
  currentIndex: number = 0; // Añadir variable para el índice actual
  showSuccessMessage = false;
  teacherAssignments: TeacherDTO[] = [];

  //Variable for alert
  messageAlert: string = '';
  stateAlert: 'save' | 'error' | 'correct' = 'save';
  showAlert: boolean = false;

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private asignatureService: SubjectService,
    private route: ActivatedRoute,
    private competenciesSubject: SubjectCompetencyService,
    private editStateService: EditStateService,
    private auth: AuthService,
    private assigmentService: TeacherAssignmentService,
    private router: Router,
    public cdr: ChangeDetectorRef
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
  }

  ngOnInit() {
    this.auth.role.subscribe((role) => {
      this.role = role;
    });

    this.isLoading = true;
    const response = this.route.snapshot.paramMap.get('id');
    if (!response) {
      console.error("The subject doesn't exist");
      this.isLoading = false;
      return;
    }

    this.id = Number(response);
    this.loadInitialData();

    if (this.isBrowser) {
      setTimeout(() => this.initializeCarousel(), 0);
    }
  }
  ngOnDestroy() {
    // Asegurarse de resetear el estado al destruir el componente
    this.editStateService.setEditState(false);

    // Limpiar el listener del carrusel si existe
    if (this.carousel?.nativeElement) {
      this.carousel.nativeElement.removeEventListener(
        'slide.bs.carousel',
        null
      );
    }
  }
  isCoordinator(): boolean {
    return this.role?.toLowerCase() === 'coordinator';
  }

  hideAlert() {
    this.showAlert = false;
  }

  saveAlert() {
    this.messageAlert = 'Guardado con exito';
    this.stateAlert = 'save';
    this.showAlert = true;
  }

  private loadInitialData(): void {
    this.editStateService.editState$.subscribe((state) => {
      this.isEdit = state;
    });
    this.asignatureService.getSubjectID(this.id).subscribe({
      next: (subject) => {
        this.actualAsignature = subject;
        this.title = subject.name;
        this.description = subject.description;
        this.loadCompetencies();
      },
      error: (error) => {
        console.error('Error loading subject:', error);
        this.isLoading = false;
      },
    });

    this.assigmentService.getAssignmentsBySubject(this.id).subscribe({
      next: (assignments) => {
        assignments.forEach((as) => {
          this.auth.getUserById(as.teacherUid).subscribe({
            next: (teacher) => {
              this.teacherAssignments.push(teacher);
            },
            error: (error) => {
              console.error('Error loading teacher:', error);
            },
          });
        });
      },
      error: (error) => {
        console.error('Error loading assignments:', error);
      },
    });
  }

  /**
   * Initiliced the carrusel for listening the current index in documenet
   * @prvate
   */
  private initializeCarousel(): void {
    if (!this.carousel?.nativeElement) return;

    setTimeout(() => {
      // Create carousel instance
      this.carouselInstance = new bootstrap.Carousel(
        this.carousel.nativeElement,
        {
          interval: false,
          ride: false,
          wrap: true,
        }
      );

      // Remove any existing event listeners first
      this.carousel.nativeElement.removeEventListener('slid.bs.carousel', null);

      // Add proper event listener with bind to keep 'this' context
      const handleCarouselSlide = (event: any) => {
        console.log('Carousel slid to:', event.to);
        this.currentIndex = event.to;
        // Force Angular to detect changes
        this.cdr.detectChanges();
      };

      // Listen to both slide events for maximum compatibility
      this.carousel.nativeElement.addEventListener('slid.bs.carousel', handleCarouselSlide);

      // Also manually set up click handlers for indicator buttons
      const indicatorButtons = document.querySelectorAll('[data-bs-target="#competencyCarousel"]');
      indicatorButtons.forEach((button: any, index: number) => {
        button.addEventListener('click', () => {
          setTimeout(() => {
            this.currentIndex = index;
            this.cdr.detectChanges();
          }, 50);
        });
      });
    }, 0);
  }

  /**
   * Lifecycle hook that is called after Angular has fully initialized
   * the component's view. This method initializes a Bootstrap carousel
   * instance if the code is running in a browser environment.
   *
   * The carousel is initialized with a 5-second interval between slides.
   * A `setTimeout` is used to ensure that the carousel is properly set up
   * after the view has been rendered.
   *
   * @remarks
   * This method relies on the `isBrowser` property to determine if the
   * code is running in a browser environment, which is necessary for
   * server-side rendering compatibility.
   */

  ngAfterViewInit() {
    if (this.isBrowser) {
      this.initializeCarousel();
      this.setupIntersectionObserver();

      // Add this new code to handle fixed indicator clicks
      setTimeout(() => {
        const fixedIndicators = document.querySelectorAll('.carousel-indicators-fixed button');
        fixedIndicators.forEach((button: any, index: number) => {
          button.addEventListener('click', () => {
            if (this.carouselInstance) {
              this.carouselInstance.to(index);
              this.currentIndex = index;
              this.cdr.detectChanges();
            }
          });
        });
      }, 500);
    }
  }
  /**
   * Retrieves the details of a specific subject by its ID and updates the component's state.
   *
   * @param id - The unique identifier of the subject to fetch.
   *
   * This method calls the `getSubjectID` method from the `asignatureService` to fetch
   * the subject details. Once the data is retrieved, it updates the `actualAsignature`,
   * `title`, and `description` properties of the component with the fetched information.
   */
  getAsignatureID(id: number): void {
    this.asignatureService.getSubjectID(id).subscribe((data) => {
      this.actualAsignature = data;
      this.title = this.actualAsignature.name;
      this.description = this.actualAsignature.description;
    });
  }

  /**
   * Loads the list of competencies associated with the current subject.
   *
   * This method retrieves the competencies by calling the `getCompetenciesByAsignature`
   * method from the `competenciesSubject` service, using the current subject's ID.
   * Once the data is successfully fetched, it updates the `listCompetency` property
   * and stops the loading indicator. If an error occurs during the process, it logs
   * the error to the console and stops the loading indicator as well.
   *
   * @private
   */
  private loadCompetencies(): void {
    this.competenciesSubject.getCompetenciesByAsignature(this.id).subscribe({
      next: (competencies) => {
        this.listCompetency = competencies;
        this.isLoading = false; // Stop loading when everything is ready
      },
      error: (error) => {
        console.error('Error loading competencies:', error);
        this.isLoading = false;
      },
    });
  }

  /**
   * Handles the change of the edit state for the component.
   * Updates the `isEdit` property, reloads the competencies, and manages
   * the behavior of the carousel instance based on the new state.
   *
   * @param state - A boolean indicating the new edit state.
   *                `true` for entering edit mode, `false` for exiting edit mode.
   *
   * Behavior:
   * - When entering edit mode (`state = true`):
   *   - Pauses the carousel instance.
   *   - Keeps the current slide active.
   * - When exiting edit mode (`state = false`):
   *   - Resumes the carousel's automatic cycling.
   */
  onEditStateChange(state: boolean) {
    this.isEdit = state;

    if (this.carouselInstance) {
      if (state) {
        // Pausar el carrusel
        this.carouselInstance.pause();
        this.carousel.nativeElement.classList.add('editing');
        // Asegurar que el carrusel está en el índice correcto
        requestAnimationFrame(() => {
          this.carouselInstance.to(this.currentIndex);
          console.log('Cambiando a slide:', this.currentIndex);
        });
      } else {
        // Al salir del modo edición
        this.carousel.nativeElement.classList.remove('editing');
        this.loadCompetencies();
        setTimeout(() => {
          this.carouselInstance.to(this.currentIndex);
          this.carouselInstance.cycle();
        });
      }
    }
  }

  goToCompetency(): void {
    const element = document.getElementById('competencySection');
    if (element) {
      element.scrollIntoView({
        behavior: 'smooth',
        block: 'start',
      });
    }
  }
  goToCreateCompetency(): void {
    this.router.navigate([`asignatures/${this.id}/subjectCompetency`], {
      queryParams: {
        subjectId: this.id,
      },
    });
  }

  goToAssignTeachers(): void {
    this.router.navigate([`asignatures/${this.id}/assignTeachers`], {
      queryParams: {
        subjectId: this.id,
      },
    });
  }

  /**
   * Returns the ID of the current subject.
   *
   * @returns The subject ID if available, or undefined if no subject is loaded.
   */
  getSubjectId(): number {
    if (this.actualAsignature) {
      return this.actualAsignature.id;
    }

    // Si no hay asignatura cargada pero tenemos un ID de la URL
    if (this.id !== -1) {
      return this.id;
    }

    return 0;
  }

  /**
   * Configura un observador para detectar cuando la sección de competencias está visible
   * y gestionar la visibilidad de los indicadores basándose en la posición de scroll.
   */
  /**
   * Configura un observador para detectar cuando la sección de competencias está visible
   * y gestionar la visibilidad de los indicadores basándose en la posición de scroll.
   */
  private setupIntersectionObserver(): void {
    if (!this.isBrowser) return;

    let scrollTimer: any = null;

    // Detector de scroll simplificado que muestra los indicadores solo cuando
    // estamos en o por debajo de la sección de competencias
    window.addEventListener('scroll', () => {
      if (scrollTimer) clearTimeout(scrollTimer);

      scrollTimer = setTimeout(() => {
        const section = document.getElementById('competencySection');
        if (!section) return;

        const rect = section.getBoundingClientRect();

        // Valor ajustado a 20px para coincidir exactamente con el comportamiento de scrollIntoView
        // Muestra los indicadores cuando la sección está en o cerca del borde superior
        if (rect.top <= 0) {
          this.isCompetencySectionVisible = true;
        } else {
          // En cualquier otro caso (estamos por encima de la sección), ocultar
          this.isCompetencySectionVisible = false;
        }
      }, 50);
    });

    // Verificación inicial con el mismo margen
    setTimeout(() => {
      const section = document.getElementById('competencySection');
      if (section) {
        const rect = section.getBoundingClientRect();
        this.isCompetencySectionVisible = rect.top <= 20;
      } else {
        console.error('No se encontró la sección de competencias');
      }
    }, 500);
  }

  // Add this method to the SubjectComponent class
  onCompetencyDeleted(competencyId: number): void {
    console.log('Competency deleted:', competencyId);

    // Show success message
    this.messageAlert = 'Competencia eliminada con éxito';
    this.stateAlert = 'save';
    this.showAlert = true;

    // Reload competencies
    this.loadCompetencies();

    // Reset edit state if needed
    this.editStateService.setEditState(false);
    this.isEdit = false;
  }
}

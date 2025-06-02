import { Component, viewChild, ElementRef, ViewChild, EventEmitter, Inject, PLATFORM_ID } from '@angular/core';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecules/molecule-back-header/molecule-back-header.component';
import { ActivatedRoute, Router } from '@angular/router';
import { SubjectDTO } from '../../models/SubjectDTO';
import { SubjectService } from '../../services/subject.service';
import { MoleculeBlockUserComponent } from '../../componentsShared/molecules/molecule-block-user/molecule-block-user.component';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { SubjectCompetencyService } from '../../services/subject_competency.service';
import { SubjectCompetency } from '../../models/SubjectCompetencyDTO';
import { TemplateCompetencyComponent } from '../../componentsShared/templates/template-competency/template-competency.component';
import { TemplateCompetencyEditComponent } from '../../componentsShared/templates/template-competency-edit/template-competency-edit.component';
import { AuthService } from '../../services/auth.service';
import { EditStateService } from '../../services/edit-state.service';
import { Carousel } from 'bootstrap';
import { LoadingComponent } from '../../componentsShared/loading/loading.component';
import { forkJoin } from 'rxjs';
import {
  TemplateListTeachersComponent
} from "../../componentsShared/templates/template-list-teachers/template-list-teachers.component";
import {TeacherAssignmentService} from "../../services/teacher_assignment.service";
import {TeacherAssignment} from "../../models/TeacherAssignmentDTO";
import {TeacherDTO} from "../../models/TeacherDTO";
declare var bootstrap: any;



@Component({
  selector: 'app-asignature',
  imports: [
      MoleculeBackHeaderComponent,
      TemplateCompetencyComponent,
      TemplateCompetencyEditComponent,
      TemplateListTeachersComponent,
      CommonModule,
      LoadingComponent

  ],
  templateUrl: './subject.component.html',
  styleUrl: './subject.component.css'
})
export class SubjectComponent {
  @ViewChild('competencyCarousel') carousel!: ElementRef;
  private carouselInstance: any;
  private readonly isBrowser: boolean;

  isLoading: boolean  = false;
  description: string = 'description';
  title: string= 'title';
  actualAsignature: SubjectDTO | null = null;
  listCompetency: SubjectCompetency[] = [];
  id: number = -1;

  isEdit: boolean = true;
  currentIndex: number = 0; // Añadir variable para el índice actual

  teacherAssignments: TeacherDTO[] = [];

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private asignatureService: SubjectService,
    private route: ActivatedRoute,
    private competenciesSubject: SubjectCompetencyService ,
    private editStateService: EditStateService,
    private auth: AuthService,
    private assigmentService:TeacherAssignmentService,
    private router: Router
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
  }
  ngOnInit() {
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


  private loadInitialData(): void {

    this.editStateService.editState$.subscribe(state => {
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
      }
    });

    this.assigmentService.getAssignmentsBySubject(this.id).subscribe({
      next: (assignments) => {

        assignments.forEach(as => {
          this.auth.getUserById(as.teacherUid).subscribe({
            next: (teacher) => {
              this.teacherAssignments.push(teacher);
            },
            error: (error) => {
              console.error('Error loading teacher:', error);
            }
          });
        });
      },
      error: (error) => {
        console.error('Error loading assignments:', error);
      }
    });
  }

  /**
   * Initiliced the carrusel for listening the current index in documenet
   * @prvate
   */
  private initializeCarousel(): void {
    setTimeout(() => {
      this.carouselInstance = new bootstrap.Carousel(this.carousel.nativeElement, {
        interval: 5000,
        ride: 'carousel',
        wrap: true
      });
      // Asegurar que las transiciones se mantienen
      const carouselElement = this.carousel.nativeElement;
      carouselElement.style.transition = 'none';
      // Escuchar eventos del carrusel
      this.carousel.nativeElement.addEventListener('slide.bs.carousel', (event: any) => {
        this.currentIndex = event.to;
        console.log('Índice actual:', this.currentIndex);
      });
    });
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
    this.asignatureService.getSubjectID(id).subscribe(data => {
      this.actualAsignature = data;
      this.title = this.actualAsignature.name;
      this.description  = this.actualAsignature.description;
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
      }
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
        block: 'start'
      });
    }
  }
  goToCreateCompetency(): void {
    this.router.navigate([`asignatures/${this.id}/subjectCompetency`], {
      queryParams: {
        subjectId: this.id
      }
    });
  }
}

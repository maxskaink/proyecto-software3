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
declare var bootstrap: any;



@Component({
  selector: 'app-asignature',
  imports: [
      MoleculeBackHeaderComponent,
      MoleculeBlockUserComponent,
      TemplateCompetencyComponent, 
      TemplateCompetencyEditComponent,
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
  role: string ="";
  name: string ="";
  currentIndex: number = 0; // Añadir variable para el índice actual

  
  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private asignatureService: SubjectService,
    private route: ActivatedRoute,
    private competenciesSubject: SubjectCompetencyService ,
    private editStateService: EditStateService,
    private auth: AuthService,
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
    // Get edit state
    this.editStateService.editState$.subscribe(state => {
      this.isEdit = state;
    });

    // Get subject data
    this.asignatureService.getSubjectID(this.id).subscribe({
      next: (subject) => {
        this.actualAsignature = subject;
        this.title = subject.name;
        this.description = subject.description;
        this.loadCompetencies(); // Load competencies after subject
      },
      error: (error) => {
        console.error('Error loading subject:', error);
        this.isLoading = false;
      }
    });

    // Get user data
    this.auth.role.subscribe(role => {
      if (role) this.role = role;
    });

    this.auth.name.subscribe(name => {
      if (name) this.name = name;
    });
  }
  private initializeCarousel(): void {
    this.carouselInstance = new bootstrap.Carousel(this.carousel.nativeElement, {
      interval: 5000,
      ride: 'carousel',
      wrap: true
    });

    // Listen for slide events
    this.carousel.nativeElement.addEventListener('slide.bs.carousel', (event: any) => {
      this.currentIndex = event.to;
    });
  }


  ngAfterViewInit() {
    // Solo inicializar el carrusel en el navegador
    if (this.isBrowser) {
      setTimeout(() => {
        this.carouselInstance = new bootstrap.Carousel(this.carousel.nativeElement, {
          interval: 5000
        });
      });
    }
  }

  /**
   * get the current aisgnatre
   * @param id ID of the subject to fetch get of url
   */
  getAsignatureID(id: number): void {
    this.asignatureService.getSubjectID(id).subscribe(data => {
      this.actualAsignature = data;
      this.title = this.actualAsignature.name;
      this.description  = this.actualAsignature.description;
    });
  }
  /**
   * load the competncies of current subject
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
  onEditStateChange(state: boolean) {
    this.isEdit = state;
    this.loadCompetencies();
    if (this.carouselInstance) {
      if (state) {
        this.carouselInstance.pause();
        // Mantener el slide actual cuando cambia a modo edición
        this.carouselInstance.to(this.currentIndex);
      } else {
        this.carouselInstance.cycle();
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
    // Navigate to subject competency with required query parameters
    this.router.navigate([`asignatures/${this.id}/subjectCompetency`], {
      queryParams: {
        subjectId: this.id
      }
    });
  }
}

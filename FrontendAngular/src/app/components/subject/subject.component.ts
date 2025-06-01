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
    this.editStateService.editState$.subscribe(state => {
      this.isEdit = state;
    });
    const response = this.route.snapshot.paramMap.get('id');
    if(!response)
      console.log("The subject doesnt exist");
    else {
      this.id = Number(response);
      if (this.id) {
        this.getAsignatureID(+this.id);
        this.loadCompetencies();
      }
    }

    this.auth.role.subscribe(role=> {
      if(role!=null)
        this.role=role;
    });
    this.auth.name.subscribe(name=>{
      if(name!=null)
        this.name=name;
    })
    this.isLoading= false;
    console.log("Cargando asignatura con id: ", this.id);
    console.log("competencia cargadas: ", this.listCompetency);
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
  
  loadCompetencies(): void{
    this.competenciesSubject.getCompetenciesByAsignature(this.id).subscribe(
      (listCompetencies) => {
        this.listCompetency = listCompetencies;
      }
    );
  }
  
  onEditStateChange(state: boolean) {
    this.isEdit = state;
    this.loadCompetencies();
    if (this.carouselInstance) {
      if (state) {
        this.carouselInstance.pause();
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

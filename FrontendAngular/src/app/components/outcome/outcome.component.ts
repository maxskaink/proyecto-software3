import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ValueChangeEvent } from '@angular/forms';
import { MoleculeSectionOptionComponent } from '../../componentsShared/molecules/molecule-section-option/molecule-section-option.component';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';
import { Action, SectionOption } from '../../models/SectionOptions';
import { Router, ActivatedRoute } from '@angular/router';
import { SubjectOutomeService } from '../../services/subject_outcome.service';
import { SubjectOutcome } from '../../models/SubjectOutcomeDTO';
import { TemplateRubricTableComponent } from '../../componentsShared/templates/template-rubric-table/template-rubric-table.component';
import {RubricDTO} from '../../models/RubricDTO';
import { LoadingComponent } from '../../componentsShared/loading/loading.component';
import {
  TemplateListTeachersComponent
} from "../../componentsShared/templates/template-list-teachers/template-list-teachers.component";
import {TeacherDTO} from "../../models/TeacherDTO";
import {AuthService} from "../../services/auth.service";
import { ViewportScroller } from '@angular/common';



@Component({
  selector: 'app-outcome',
  imports: [CommonModule,
    FormsModule,
    MoleculeSectionOptionComponent,
    TemplateHeaderTitleComponent,
    TemplateRubricTableComponent,
    LoadingComponent,
    TemplateListTeachersComponent
    ],
  templateUrl: './outcome.component.html',
  styleUrl: './outcome.component.css'
})
export class OutcomeComponent implements OnInit {
  currentOutcome: SubjectOutcome = {} as SubjectOutcome;
  currentRubric: RubricDTO | null = {} as RubricDTO;
  title: string = 'RA';
  description: string = 'Las RAs son el conjunto .';
  outcomeId: number = 0;
  isEditDescription: boolean = false;
  isLoading:boolean= false;

  options: SectionOption[] = [
    {
      title: 'Evaluadores',
      description: 'Toda RA tiene sus propios evaluadores, ¡Dales un vistazo!',
      showButtonOne: true,
      buttonTextOne: 'Ver evaluadores',
      actionOne:{
        type: 'scroll',
        value: 'evaluators'
      },
      showButtonTwo: true,
      buttonTextTwo: 'Gestionar Evaluadores',

    },
    {
      title: 'Rubrica',
      description: 'Nuestras rubricas se manejan por criterios y niveles, los criterios te indicaran  el tema evaluado mientras que los niveles que tanto los domina',
      showButtonOne: true,
      buttonTextOne: 'Ver rubrica',
      actionOne:{
        type: 'scroll',
        value: 'rubryc'
      },
      showButtonTwo: true,
      buttonTextTwo: 'Editar rubrica',

    },
  ];

  evaluators: TeacherDTO[] =[];

  constructor(private router: Router,
    private route: ActivatedRoute,
    private outComeService: SubjectOutomeService,
    private authService: AuthService,
    private viewportScroller: ViewportScroller
  ){
    }

  ngOnInit(): void {
    // Scroll to top when component initializes
     // Scroll to top when component initializes
    this.viewportScroller.scrollToPosition([0, 0]);
    this.isLoading= true;
    this.route.queryParams.subscribe(params => {
      if (params['outcomeId']) {
        this.outcomeId = +params['outcomeId'];
        this.getOutcome();
        this.isLoading =false;

        this.getEvaluators();

      } else {
        console.error('No outcome ID provided');
        this.router.navigate(['/home']); // or handle missing ID case
        this.isLoading =false;
      }
    });
  }

  getEvaluators(){
    this.evaluators = [
      {
        academicTitle: "postgrado",
        identification: 123456789,
        identificationType: "CC",
        lastName: "Calambas",
        name: "Miguel",
        role: "Teacher",
        typeTeacher: "catedratico",
      },
      {
        academicTitle: "postgrado",
        identification: 123456789,
        identificationType: "CC",
        lastName: "Calambas",
        name: "Miguel",
        role: "Teacher",
        typeTeacher: "catedratico",
      },
      {
        academicTitle: "postgrado",
        identification: 123456789,
        identificationType: "CC",
        lastName: "Calambas",
        name: "Miguel",
        role: "Teacher",
        typeTeacher: "catedratico",
      },
    ]
    //Traer solo evaluadores asignados
  }

  editDescription(): void{
    this.isEditDescription = !this.isEditDescription;
  }
  saveDescription(): void {
    // Validar que hay una descripción
    if (!this.description?.trim()) {
      console.error('La descripción no puede estar vacía');
      return;
    }

    // Crear el objeto con los datos actualizados
    const updatedOutcome: SubjectOutcome = {
      ...this.currentOutcome,
      description: this.description
    };

    // Llamar al servicio para actualizar
    this.outComeService.updateOutcome(this.currentOutcome.id, updatedOutcome).subscribe({
      next: (response) => {
        console.log('Resultado de aprendizaje actualizado:', response);
        this.currentOutcome = response;
        this.description = response.description;
        this.editDescription(); // Cerrar el modo edición
      },
      error: (error) => {
        console.error('Error al actualizar el resultado de aprendizaje:', error);
        // Opcional: Revertir cambios en caso de error
        this.description = this.currentOutcome.description;
      }
    });
  }

  getOutcome(): void {
    this.outComeService.getOutcomeById(this.outcomeId).subscribe({
      next: (data: SubjectOutcome) => {
        this.currentOutcome = data;
        this.title = `RA ${this.outcomeId + 1}`; // Corrección aquí
        this.description = data.description;
        if(data.rubric){
          this.currentRubric = data.rubric;
        }else{
          this.currentRubric = null;
        }

//        console.log(data);
//        console.log(this.currentOutcome);
//        console.log("rubrica: " +this.currentRubric)
      },
      error: (error) => {
        console.error('Error al obtener el outcome:', error);
      }
    });
  }

  handleBotonUno() {
    console.log('Se hizo clic en el botón 1');
    // Aquí puedes hacer redirección, abrir un modal, etc.
  }

  handleBotonDos() {
    console.log('Se hizo clic en el botón 2');
  }

}

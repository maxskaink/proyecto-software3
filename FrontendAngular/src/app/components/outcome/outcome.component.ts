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


@Component({
  selector: 'app-outcome',
  imports: [CommonModule,
    FormsModule,
    MoleculeSectionOptionComponent,
    TemplateHeaderTitleComponent,
    TemplateRubricTableComponent 
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

  constructor(private router: Router,
    private route: ActivatedRoute,
    private outComeService: SubjectOutomeService){}

  ngOnInit(): void {
    this.getOutcome();
    /**
    this.route.queryParams.subscribe(params => {
      if (params['outcomeId']) {
        this.outcomeId = +params['outcomeId'];
        this.getOutcome();
      } else {
        console.error('No outcome ID provided');
        this.router.navigate(['/home']); // or handle missing ID case
      }
    });
     */
  }
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

getOutcome(): void {
  this.outComeService.getOutcomeById(1).subscribe({
    next: (data: SubjectOutcome) => {
      this.currentOutcome = data;
      this.title = `RA ${this.outcomeId + 1}`; // Corrección aquí
      this.description = data.description;
      if(data.rubric){
        this.currentRubric = data.rubric;
      }else{
        this.currentRubric = null;
      }
  
      console.log(data);
      console.log(this.currentOutcome);
      console.log(this.currentRubric)
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

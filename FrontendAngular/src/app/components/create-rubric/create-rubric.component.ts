import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';
import { TemplateRubricCreateCriterionComponent } from '../../componentsShared/templates/template-rubric-create-criterion/template-rubric-create-criterion.component';
import { ActivatedRoute } from '@angular/router';
import { MoleculeRubricLevelForCreateComponent } from '../../componentsShared/molecules/molecule-rubric-level-for-create/molecule-rubric-level-for-create.component';
import { CriterionDTO } from '../../models/CirterionDTO';
import { CriterionEntity } from '../../models/CriterionEntity';
import { TemplateRubricRowComponent } from '../../componentsShared/templates/template-rubric-row/template-rubric-row.component';

@Component({
  selector: 'app-create-rubric',
  imports: [CommonModule, 
    TemplateHeaderTitleComponent, 
    TemplateRubricCreateCriterionComponent,
    TemplateRubricRowComponent
  ],
  templateUrl: './create-rubric.component.html',
  styleUrl: './create-rubric.component.css'
})
export class CreateRubricComponent {
  idRubric: number = -1;
  criterioIsEdit: boolean = false;
  readonly MAX_CRITERIA: number = 5; // Add constant for max criteria
  editingCriterion: CriterionEntity | null = null; // Add this to track which criterion is being edited
  constructor(private route: ActivatedRoute) {}
  listCriterion: CriterionEntity[] = [];
  error= {
    weight: '',
    maxCriteria:''
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.idRubric = parseInt(params['idRubric'] || '-1', 10);
      console.log('Rubric ID:', this.idRubric);
    });
  }
  validateCriterions():boolean{
    let totalPercentage: number = 0; 
    for(const criterion of this.listCriterion){
       totalPercentage += criterion.weight;
    }
    if(totalPercentage != 100){
      this.error.weight = 'Los criterios deben sumar en total 100%'
      return false;
    }
      
    return true;
  }

  onCriterionAdded(criterion: CriterionEntity): void {
    if (this.editingCriterion) {
      const index = this.listCriterion.findIndex(c => c === this.editingCriterion);
      if (index !== -1) {
        this.listCriterion[index] = criterion;
      }
      this.editingCriterion = null;
      this.criterioIsEdit = false;
    } else {
      if (this.listCriterion.length >= this.MAX_CRITERIA) {
        this.error.maxCriteria = 'No se pueden agregar más de 5 criterios';
        return;
      }
      this.listCriterion.push(criterion);
    }
  }
  handleEditCriterion(criterion: CriterionEntity): void {
    this.criterioIsEdit = true; // Set the flag to indicate editing mode
    this.editingCriterion = criterion; // Store the criterion being edited
  }
  handleDeleteCriterion(criterion: CriterionEntity): void {
    // Lógica para eliminar el criterio
    const index = this.listCriterion.indexOf(criterion);
    if (index > -1) {
      this.listCriterion.splice(index, 1);
    }
    console.log('Criterio eliminado:', criterion);
  }

}

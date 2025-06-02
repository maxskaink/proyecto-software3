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

  constructor(private route: ActivatedRoute) {}
  listCriterion: CriterionEntity[] = [];

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.idRubric = parseInt(params['idRubric'] || '-1', 10);
      console.log('Rubric ID:', this.idRubric);
    });
  }
  onCriterionAdded(criterion: CriterionEntity): void {
    this.listCriterion.push(criterion);
    console.log('Updated criteria list:', this.listCriterion);
  }
  

}

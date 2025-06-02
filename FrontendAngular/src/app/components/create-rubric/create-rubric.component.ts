import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';
import { TemplateRubricCreateCriterionComponent } from '../../componentsShared/templates/template-rubric-create-criterion/template-rubric-create-criterion.component';
import { ActivatedRoute } from '@angular/router';
import { MoleculeRubricLevelForCreateComponent } from '../../componentsShared/molecules/molecule-rubric-level-for-create/molecule-rubric-level-for-create.component';

@Component({
  selector: 'app-create-rubric',
  imports: [CommonModule, 
    TemplateHeaderTitleComponent, 
    TemplateRubricCreateCriterionComponent,
  ],
  templateUrl: './create-rubric.component.html',
  styleUrl: './create-rubric.component.css'
})
export class CreateRubricComponent {
  idRubric: number = -1;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.idRubric = parseInt(params['idRubric'] || '-1', 10);
      console.log('Rubric ID:', this.idRubric);
    });
  }
}

import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';
import { TemplateRubricCreateCriterionComponent } from '../../componentsShared/templates/template-rubric-create-criterion/template-rubric-create-criterion.component';

@Component({
  selector: 'app-create-rubric',
  imports: [CommonModule, TemplateHeaderTitleComponent, TemplateRubricCreateCriterionComponent],
  templateUrl: './create-rubric.component.html',
  styleUrl: './create-rubric.component.css'
})
export class CreateRubricComponent {
  constructor(){}
  
}

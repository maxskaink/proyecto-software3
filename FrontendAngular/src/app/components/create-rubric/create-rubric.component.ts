import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { TemplateHeaderTitleComponent } from '../../componentsShared/templates/template-header-title/template-header-title.component';

@Component({
  selector: 'app-create-rubric',
  imports: [CommonModule, TemplateHeaderTitleComponent],
  templateUrl: './create-rubric.component.html',
  styleUrl: './create-rubric.component.css'
})
export class CreateRubricComponent {
  constructor(){}
  
}

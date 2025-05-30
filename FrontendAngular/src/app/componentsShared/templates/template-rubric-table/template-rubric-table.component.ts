import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { TemplateRubricRowComponent } from '../template-rubric-row/template-rubric-row.component';
import { RubricDTO } from '../../../models/RubricDTO';

@Component({
  selector: 'app-template-rubric-table',
  imports: [CommonModule, TemplateRubricRowComponent],
  templateUrl: './template-rubric-table.component.html',
  styleUrl: './template-rubric-table.component.css'
})
export class TemplateRubricTableComponent implements OnInit{
  
  @Input() rubric: RubricDTO = {} as RubricDTO; 
  descriptionRubric: string = '';
  // Assuming RubricDTO is imported from the correct path
  ngOnInit(): void {
   if( this.rubric ) {
      this.descriptionRubric = this.rubric.description;
    }
    else {
      this.descriptionRubric = 'No hay descripción disponible para esta rúbrica.';
  
    }
  }
}

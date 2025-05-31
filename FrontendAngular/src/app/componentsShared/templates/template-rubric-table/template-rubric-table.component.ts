import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { TemplateRubricRowComponent } from '../template-rubric-row/template-rubric-row.component';
import { RubricDTO } from '../../../models/RubricDTO';
import { CriterionDTO } from '../../../models/CirterionDTO';


@Component({
  selector: 'app-template-rubric-table',
  imports: [CommonModule, TemplateRubricRowComponent, ],
  templateUrl: './template-rubric-table.component.html',
  styleUrl: './template-rubric-table.component.css'
})
export class TemplateRubricTableComponent implements OnInit{
  
  @Input() rubric: RubricDTO| null = {} as RubricDTO ; 
  @Input() isCreated: boolean = false; 
  descriptionRubric: string = '';
  criterions: CriterionDTO[] | null = {} as CriterionDTO[];
  // Assuming RubricDTO is imported from the correct path
  ngOnInit(): void {
    if (this.rubric && this.rubric.description) {
      this.descriptionRubric = this.rubric.description;
      if( this.rubric.criteria && this.rubric.criteria.length > 0) {
        this.criterions = this.rubric.criteria;
      }

    }
  }

}

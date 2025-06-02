import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { TemplateRubricRowComponent } from '../template-rubric-row/template-rubric-row.component';
import { RubricDTO } from '../../../models/RubricDTO';
import { CriterionDTO } from '../../../models/CirterionDTO';
import { CriterionEntity } from '../../../models/CriterionEntity';
import { LevelEntity } from '../../../models/LevelEntity';


@Component({
  selector: 'app-template-rubric-table',
  imports: [CommonModule, TemplateRubricRowComponent, ],
  templateUrl: './template-rubric-table.component.html',
  styleUrl: './template-rubric-table.component.css'
})
export class TemplateRubricTableComponent implements OnInit{
  
  @Input() rubric: RubricDTO| null = {} as RubricDTO ; 
  descriptionRubric: string = '';
  
  criterionEntities: CriterionEntity[] = [];
  
  ngOnInit(): void {
    if (this.rubric && this.rubric.description) {
      this.descriptionRubric = this.rubric.description;
      if (this.rubric.criteria && this.rubric.criteria.length > 0) {
        this.criterionEntities = this.rubric.criteria.map(dto => this.mapCriterionDTOToEntity(dto));
      }
    }
  }

  private mapCriterionDTOToEntity(criterionDTO: CriterionDTO): CriterionEntity {
    return {
      name: criterionDTO.name,
      weight: criterionDTO.weight,
      levels: criterionDTO.levels.map(level => ({
        description: level.description,
        percentage: level.percentage
      } as LevelEntity))
    };
  }

}

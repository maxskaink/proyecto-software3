import { Component, Input, OnInit } from '@angular/core';
import { RubricDTO } from '../../../models/RubricDTO';
import { CommonModule } from '@angular/common';
import { LevelDTO } from '../../../models/LevelDTO';
import { CriterionDTO } from '../../../models/CirterionDTO';

@Component({
  selector: 'app-template-rubric-row',
  imports: [CommonModule],
  templateUrl: './template-rubric-row.component.html',
  styleUrl: './template-rubric-row.component.css'
})
export class TemplateRubricRowComponent implements OnInit{
  @Input() criteria: CriterionDTO= {} as CriterionDTO;

  name: string = 'name Example';
  constructor(){} 
  ngOnInit(): void {
    if (this.criteria && this.criteria.name) {
      this.name= this.criteria.name;
    }
  }

}

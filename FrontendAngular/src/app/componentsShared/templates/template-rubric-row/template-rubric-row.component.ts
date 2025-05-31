import { Component, Input, OnInit } from '@angular/core';
import { RubricDTO } from '../../../models/RubricDTO';
import { CommonModule } from '@angular/common';
import { LevelDTO } from '../../../models/LevelDTO';
import { CriterionDTO } from '../../../models/CirterionDTO';
import { MoleculeLevelBoxComponent } from '../../molecules/molecule-level-box/molecule-level-box.component';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-template-rubric-row',
  imports: [CommonModule, MoleculeLevelBoxComponent, FormsModule],
  templateUrl: './template-rubric-row.component.html',
  styleUrl: './template-rubric-row.component.css'
})
export class TemplateRubricRowComponent implements OnInit{
  @Input() criteria: CriterionDTO= {} as CriterionDTO;
  levels: LevelDTO[] | null = {} as LevelDTO[]; 
  name: string = 'name Example';
  weight: number = 0; 
  constructor(){} 
  ngOnInit(): void {
    if (this.criteria && this.criteria.name) {
      this.name= this.criteria.name;
      this.levels= this.criteria.levels;
      this.weight = this.criteria.weight;
    }
  }

}

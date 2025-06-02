import { Component, Input, OnInit } from '@angular/core';
import { RubricDTO } from '../../../models/RubricDTO';
import { CommonModule } from '@angular/common';
import { LevelDTO } from '../../../models/LevelDTO';
import { CriterionDTO } from '../../../models/CirterionDTO';
import { MoleculeLevelBoxComponent } from '../../molecules/molecule-level-box/molecule-level-box.component';
import { FormsModule } from '@angular/forms';
import { CriterionEntity } from '../../../models/CriterionEntity';
import { LevelEntity } from '../../../models/LevelEntity';

@Component({
  selector: 'app-template-rubric-row',
  imports: [CommonModule, MoleculeLevelBoxComponent, FormsModule],
  templateUrl: './template-rubric-row.component.html',
  styleUrl: './template-rubric-row.component.css'
})
export class TemplateRubricRowComponent implements OnInit{
  @Input() criterion: CriterionEntity= {} as CriterionEntity;
  levels: LevelEntity[] | null = {} as LevelEntity[]; 
  name: string = 'name Example';
  weight: number = 0; 
  constructor(){} 
  ngOnInit(): void {
    if (this.criterion && this.criterion.name) {
      this.name= this.criterion.name;
      this.levels= this.criterion.levels;
      this.weight = this.criterion.weight;
    }

  }

}

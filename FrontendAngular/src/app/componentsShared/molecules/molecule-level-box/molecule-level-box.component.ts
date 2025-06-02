import { Component, Input, OnInit } from '@angular/core';
import { LevelDTO } from '../../../models/LevelDTO';
import { LevelEntity } from '../../../models/LevelEntity';

@Component({
  selector: 'app-molecule-level-box',
  imports: [],
  templateUrl: './molecule-level-box.component.html',
  styleUrl: './molecule-level-box.component.css'
})
export class MoleculeLevelBoxComponent implements OnInit{
  
  @Input() level: LevelEntity | null = {} as LevelEntity; 
  description: string = '';
  percentage: number = 0; 
  
  

  ngOnInit(): void {
    if (this.level && this.level.description) {
      this.description = this.level.description;
      this.percentage = this.level.percentage;
    }
  }

}

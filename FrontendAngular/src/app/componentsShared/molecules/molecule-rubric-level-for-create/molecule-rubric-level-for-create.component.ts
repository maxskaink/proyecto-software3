import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LevelDTO } from '../../../models/LevelDTO';
import { LevelEntity } from '../../../models/LevelEntity';

@Component({
  selector: 'app-molecule-rubric-level-for-create',
  imports: [CommonModule, FormsModule],
  templateUrl: './molecule-rubric-level-for-create.component.html',
  styleUrls: [
    './molecule-rubric-level-for-create.component.css',
    '../molecule-level-box/molecule-level-box.component.css'
  ]
})
export class MoleculeRubricLevelForCreateComponent implements OnInit{
  ngOnInit(): void {
    if (this.level) {
      this.description = this.level.description || '';
      this.percentage = this.level.percentage || 0;
    }
  }
  description: string = '';
  percentage: number = 0; 
  showGreenDiv: boolean = false;
  showRedDiv: boolean = false;
  error: string = '';
  @Output() greenIndicatorClicked = new EventEmitter<number>();
  @Output() redIndicatorClicked = new EventEmitter<number>();
  @Input() level!: LevelEntity;
  @Input() index!: number;
  @Output() levelChange = new EventEmitter<{index: number, level: LevelEntity}>();
  /**
   * Method for manage the event level hover, make the div green and red visible
   * @returns void
   */
  onLevelHover(): void {
    this.showGreenDiv = true;
    this.showRedDiv = true;
  }
  /**
   * Method for manage the event level leave, hide the divs
   * @returns void
   */
  onLevelLeave(): void {
    this.showGreenDiv = false;
    this.showRedDiv = false;
  } 

  /**
   * Method for manage the click on the green indicator, emit an event and log a message
   */
  onGreenIndicatorClick(): void {
    this.greenIndicatorClicked.emit(this.index);
  }
  /**
   * Method for manage the click on the red indicator, emit an event and log a message
   */
  onRedIndicatorClick(): void {
    this.redIndicatorClicked.emit(this.index);
  }
  onDescriptionChange(event: any): void {
    if (this.level.description.length >= 40) {
      this.error = 'El nombre no puede exceder los 40 caracteres';
      this.level.description = this.level.description.substring(0, 50);
    } else {
      this.error = '';
    }
    this.levelChange.emit({index: this.index, level: this.level});
  }

  onPercentageChange(event: any): void {
    this.levelChange.emit({index: this.index, level: this.level});
  }
  /**
   * Method to validate the level before submission 
   *  
   * */
  
  validateLevel(): boolean {
    if (!this.description.trim()) {
      this.error = 'El nombre no puede estar vacío';
      return false;
    }
    if (this.description.length > 50) {
      this.error = 'El nombre no puede exceder los 50 caracteres';
      return false;
    }
    return true;
  }
}

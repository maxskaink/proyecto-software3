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
      this.percentage = this.level.percentage || 10;
    }
  }
  description: string = '';
  percentage: number = 10; 
  showGreenDiv: boolean = false;
  showRedDiv: boolean = false;
  error: string = '';
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
   * Method for manage the click on the red indicator, emit an event and log a message
   */
  onRedIndicatorClick(): void {
    this.redIndicatorClicked.emit(this.index);
  }
  
  onDescriptionChange(event: string): void {
    // Actualiza la descripción en el nivel
    this.level.description = event;
    if (this.level.description.length >= 40) {
      this.error = 'El nombre no puede exceder los 40 caracteres';
      this.level.description = this.level.description.substring(0, 40);
    } else {
      this.error = '';
    }
    // Emite el cambio
    this.levelChange.emit({index: this.index, level: this.level});
  }
  onPercentageChange(event: number): void {
    // Valida y actualiza el porcentaje
    if (event > 100) {
      this.level.percentage = 100;
    } else if (event < 0) {
      this.level.percentage = 1;
    } else {
      this.level.percentage = event;
    }
    // Emite el cambio
    this.levelChange.emit({index: this.index, level: this.level});
  }
  /**
   * Method to validate the level before submission 
   *  
   * */
  
  validateLevel(): boolean {
    if (!this.level.description?.trim()) {
      this.error = 'El nombre no puede estar vacío';
      return false;
    }
    if (this.level.description.length > 40) {
      this.error = 'El nombre no puede exceder los 40 caracteres';
      return false;
    }
    return true;
  }

  validatePercentage(event: any): void {
    const value = parseInt(event.target.value);
    if (value > 100) {
        this.level.percentage = 100;
    } else if (value < 0) {
        this.level.percentage = 1;
    }
  }

 

}

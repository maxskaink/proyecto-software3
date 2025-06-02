import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-molecule-rubric-level-for-create',
  imports: [CommonModule, FormsModule],
  templateUrl: './molecule-rubric-level-for-create.component.html',
  styleUrls: [
    './molecule-rubric-level-for-create.component.css',
    '../molecule-level-box/molecule-level-box.component.css'
  ]
})
export class MoleculeRubricLevelForCreateComponent {
  description: string = '';
  percentage: number = 0; 
  name: string = '';
  showGreenDiv: boolean = false;
  showRedDiv: boolean = false;
  error: string = '';
  @Output() greenIndicatorClicked = new EventEmitter<void>();
  @Output() redIndicatorClicked = new EventEmitter<void>();
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
    this.greenIndicatorClicked.emit();
    console.log('Green indicator clicked!');
  }
  /**
   * Method for manage the click on the red indicator, emit an event and log a message
   */
  onRedIndicatorClick(): void {
    this.redIndicatorClicked.emit();
    console.log('Red indicator clicked! Resetting values.');
  }
  /*
    * Method for manage the change of the name input, validate the length and update the error message
    * @param event - The input event containing the new value
    */
  onNameChange(event: any): void {
    if (this.name.length >= 50) {
      this.error = 'El nombre no puede exceder los 50 caracteres';
      this.name = this.name.substring(0, 50);
    } 
    else {
      this.error = '';
    }
  }
  /**
   * Method to validate the level before submission 
   *  
   * */
  validateLevel(): boolean {
    if (!this.name.trim()) {
      this.error = 'El nombre no puede estar vacío';
      return false;
    }
    if (this.name.length > 50) {
      this.error = 'El nombre no puede exceder los 50 caracteres';
      return false;
    }
    return true;
  }
}

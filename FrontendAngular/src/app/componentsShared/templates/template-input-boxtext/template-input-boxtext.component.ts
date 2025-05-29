import { CommonModule } from '@angular/common';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-template-input-boxtext',
  imports: [CommonModule, FormsModule],
  templateUrl: './template-input-boxtext.component.html',
  styleUrl: './template-input-boxtext.component.css'
})
export class TemplateInputBoxtextComponent {
  @Input() description: string = '';
  @Output() saveClicked = new EventEmitter<string>();
  
  inputValue: string = '';
  touched: boolean = false;
  
  onBlur() {
    this.touched = true;
  }
  
  onSave() {
    if (this.inputValue.trim()) {
      this.saveClicked.emit(this.inputValue);
    } else {
      this.touched = true;
    }
  }
}
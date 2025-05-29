import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TemplateInputBoxtextComponent } from '../template-input-boxtext/template-input-boxtext.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-template-select-input-boxtext',
  imports: [CommonModule, FormsModule, TemplateInputBoxtextComponent],
  templateUrl: './template-select-input-boxtext.component.html',
  styleUrl: './template-select-input-boxtext.component.css'
})
export class TemplateSelectInputBoxtextComponent {
  @Input() textareaDescription?: string = '';
  @Input() options: string[] = [];
  @Input() selectDescription?: string = '';
  @Input() selectPlaceholder?: string = 'Selecciona una opción';
  
  // Change the event to emit an object with both values
  @Output() saveClicked = new EventEmitter<{description: string, option: string}>();

  selectedOption: string = '';
  inputValue: string = '';
  selectTouched: boolean = false;

  onSelectBlur() {
    this.selectTouched = true; // Mark the select as touched when it loses focus
  }

  onSave(data:string) {
    this.inputValue = data; // Update inputValue with the emitted data from the textarea

    if (!this.inputValue.trim() ) {
      console.error('Description  option is invalid');
      return false;
    }
  
    if(this.selectedOption === '')
    {
      this.selectTouched = true;
    }

    this.saveClicked.emit({
      description: this.inputValue,
      option: this.selectedOption
    });
    
    return true;
  }
}
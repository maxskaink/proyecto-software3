import { CommonModule } from '@angular/common';
import { Component, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { EventEmitter } from 'stream';

@Component({
  selector: 'app-template-input-boxtext',
  imports: [CommonModule, FormsModule],
  templateUrl: './template-input-boxtext.component.html',
  styleUrl: './template-input-boxtext.component.css'
})
export class TemplateInputBoxtextComponent {
  @Input() description: string = '';
  //@Output() blockClick = new EventEmitter<any>();
  inputValue: string= '';
  touched: boolean = false;
  onBlur() {
    this.touched= true;
  }
}


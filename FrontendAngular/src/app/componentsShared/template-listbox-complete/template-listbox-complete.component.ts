import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

export interface TextBlock {
  title: string;
  description: string;
  route?: string;
}

@Component({
  selector: 'app-template-listbox-complete',
  imports: [CommonModule],
  templateUrl: './template-listbox-complete.component.html',
  styleUrl: './template-listbox-complete.component.css'
})
export class TemplateListboxCompleteComponent {
  @Input() textBlock: TextBlock[] = [];
  @Output() blockClick = new EventEmitter<TextBlock>();

  onBlockClick(block: TextBlock) {
    this.blockClick.emit(block);
  }
}
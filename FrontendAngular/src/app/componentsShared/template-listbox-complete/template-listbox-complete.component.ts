import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-template-listbox-complete',
  imports: [CommonModule],
  templateUrl: './template-listbox-complete.component.html',
  styleUrl: './template-listbox-complete.component.css'
})
export class TemplateListboxCompleteComponent {
  @Input() textBlock: any;
}

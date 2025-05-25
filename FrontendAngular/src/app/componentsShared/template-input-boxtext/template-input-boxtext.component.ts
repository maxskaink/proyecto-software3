import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-template-input-boxtext',
  imports: [CommonModule],
  templateUrl: './template-input-boxtext.component.html',
  styleUrl: './template-input-boxtext.component.css'
})
export class TemplateInputBoxtextComponent {
  @Input() description: string = '';
}

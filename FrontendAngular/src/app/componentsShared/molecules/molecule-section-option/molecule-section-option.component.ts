import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {  SectionOption } from '../../../models/SectionOptions';
import { Router } from '@angular/router';
import { Action } from '../../../models/Action';
@Component({
  selector: 'app-molecule-section-option',
  imports: [CommonModule, FormsModule],
  templateUrl: './molecule-section-option.component.html',
  styleUrl: './molecule-section-option.component.css'
})
export class MoleculeSectionOptionComponent {
  @Input() data!: SectionOption;
  @Output() buttonOneClick = new EventEmitter<void>();
  @Output() buttonTwoClick = new EventEmitter<void>();

  constructor(private router: Router){}
  onButtonOneClick(action: any) {
    this.ejecutarAccion(action);
  }
  
  onButtonTwoClick(action: any) {
    this.ejecutarAccion(action);
  }
  
  ejecutarAccion(action: Action) {
    switch (action.type) {
      case 'navigate':
        if (action.params || action.queryParams) {
          this.router.navigate([action.value], {
            queryParams: action.queryParams,
            queryParamsHandling: 'merge'
          });
        } else {
          this.router.navigateByUrl(action.value);
        }
        break;
      case 'modal':

        break;
      case 'scroll':
        const el = document.getElementById(action.value);
        if (el) el.scrollIntoView({ behavior: 'smooth' });
        break;
      default:
        
    }
  }
  
}

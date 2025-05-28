import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SectionOption } from '../../models/SectionOptions';
import { Router } from '@angular/router';
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
  onButtonOneClick() {
    console.log("Use button one");
    this.buttonOneClick.emit();
  }

  onButtonTwoClick() {
    console.log("Use button two");
    this.buttonTwoClick.emit();
  }
  handleBotonUno(action: any) {
    this.ejecutarAccion(action);
  }
  
  handleBotonDos(action: any) {
    this.ejecutarAccion(action);
  }
  
  ejecutarAccion(action: { type: string, value: string }) {
    switch (action.type) {
      case 'navigate':
        this.router.navigateByUrl(action.value);
        break;
      case 'modal':

        break;
      case 'scroll':
        const el = document.getElementById(action.value);
        if (el) el.scrollIntoView({ behavior: 'smooth' });
        break;
      default:
        console.warn('Tipo de acción no soportado');
    }
  }
  
}

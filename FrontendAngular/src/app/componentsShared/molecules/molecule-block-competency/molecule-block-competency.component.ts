import { CommonModule } from '@angular/common';
import { Component, Input, Output } from '@angular/core';

@Component({
  selector: 'app-molecule-block-competency',
  imports: [CommonModule],
  templateUrl: './molecule-block-competency.component.html',
  styleUrl: './molecule-block-competency.component.css'
})
export class MoleculeBlockCompetencyComponent {
  @Input() title: string = 'Ejemplo titulo';
  @Input() description: string = 'Ejemplo descripcion';
  @Output() blockClick: any; 
  onDelete() {
    // lógica para eliminar el bloque
    console.log('Eliminar bloque');
  }
  
}

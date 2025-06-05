import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-molecule-block-competency',
  imports: [CommonModule],
  templateUrl: './molecule-block-competency.component.html',
  styleUrl: './molecule-block-competency.component.css'
})
export class MoleculeBlockCompetencyComponent {
  @Input() title: string = 'Ejemplo titulo';
  @Input() description: string = 'Ejemplo descripcion';
  @Input() isCreate: boolean = false;
  @Output() delete = new EventEmitter<void>();
  @Output() create = new EventEmitter<void>();

  onDelete(): void {
    this.delete.emit();
  }

  onCreate(): void {
    this.create.emit();
  }
}



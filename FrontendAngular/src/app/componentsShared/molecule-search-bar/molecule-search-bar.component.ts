import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
    selector: 'app-molecule-search-bar',
    imports: [CommonModule],
    templateUrl: './molecule-search-bar.component.html',
    styleUrl: './molecule-search-bar.component.css'
})
export class MoleculeSearchBarComponent {
  @Input() placeholder: string = 'Buscar...';
  @Output() search = new EventEmitter<string>();

  onInputChange(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.search.emit(value);
  }
}

import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MoleculeSearchBarComponent } from '../molecule-search-bar/molecule-search-bar.component';

@Component({
  selector: 'app-template-block-grid',
  imports: [CommonModule, MoleculeSearchBarComponent],
  templateUrl: './template-block-grid.component.html',
  styleUrl: './template-block-grid.component.css'
})
export class TemplateBlockGridComponent {

}

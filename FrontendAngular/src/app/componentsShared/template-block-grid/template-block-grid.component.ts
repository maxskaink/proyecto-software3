import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { MoleculeSearchBarComponent } from '../molecule-search-bar/molecule-search-bar.component';


@Component({
  selector: 'app-template-block-grid',
  imports: [CommonModule, MoleculeSearchBarComponent],
  templateUrl: './template-block-grid.component.html',
  styleUrl: './template-block-grid.component.css'
})
export class TemplateBlockGridComponent implements OnInit {
  wordSearch: string = ';';
  @Input() listBlocks: any; // Under construction, should be replaced with a specific type later
  
  constructor() {}

  ngOnInit(): void {
    // Initialize the listBlocks if not provided
  }

  onSearch(value: string): void {
    this.wordSearch = value;
  }
 
}

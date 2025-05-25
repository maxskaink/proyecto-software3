import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MoleculeSearchBarComponent } from '../molecule-search-bar/molecule-search-bar.component';
import { ProgramCompetencyService } from '../../services/program-competency.service';

@Component({
  selector: 'app-template-block-grid',
  imports: [CommonModule, MoleculeSearchBarComponent],
  templateUrl: './template-block-grid.component.html',
  styleUrl: './template-block-grid.component.css'
})
export class TemplateBlockGridComponent implements OnInit {
  wordSearch: string = ';';
  
  constructor(private serviceCompProgram: ProgramCompetencyService) {}

  ngOnInit(): void {
    // lógica de inicialización
  }

  onSearch(value: string): void {
    this.wordSearch = value;
  }
  getCompetencyProgram(): void{
    this.serviceCompProgram.getAll
  }
}

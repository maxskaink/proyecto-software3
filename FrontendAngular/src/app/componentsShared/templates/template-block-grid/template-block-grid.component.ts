import { ProgramCompetency } from './../../../models/ProgramCompetencyDTO';
import { CommonModule } from '@angular/common';
import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { MoleculeSearchBarComponent } from '../../molecules/molecule-search-bar/molecule-search-bar.component';
import { MoleculeBlockCompetencyComponent } from "../../molecules/molecule-block-competency/molecule-block-competency.component";

interface TextCompetency {
  title: string;
  outcomeDescription: string;
}

@Component({
  selector: 'app-template-block-grid',
  imports: [CommonModule, MoleculeSearchBarComponent, MoleculeBlockCompetencyComponent],
  templateUrl: './template-block-grid.component.html',
  styleUrl: './template-block-grid.component.css'
})
export class TemplateBlockGridComponent implements OnInit {
  wordSearch: string = '';
  @Input() ProgramCompetencies: ProgramCompetency[] = [];
  
  // Add Output EventEmitters for create and delete operations
  @Output() createCompetencyEvent = new EventEmitter<void>();
  @Output() deleteCompetencyEvent = new EventEmitter<number>();
  
  textCompetencies: TextCompetency[] = [];
  
  constructor() {}

  ngOnInit(): void {
    // Map ProgramCompetencies to TextCompetency objects
    this.mapCompetencies();
  }

  mapCompetencies(): void {
    // Clear any existing mappings
    this.textCompetencies = [];
    console.log('Mapping ProgramCompetencies to TextCompetencies');
    console.log('ProgramCompetencies:', this.ProgramCompetencies);
    console.log('Initial textCompetencies:', this.textCompetencies);
    
    // Map each ProgramCompetency to a TextCompetency
    if (this.ProgramCompetencies && this.ProgramCompetencies.length) {
      this.textCompetencies = this.ProgramCompetencies.map(comp => ({
        title: comp.description || 'Unnamed Competency', // Use a default if name is missing
        outcomeDescription: comp.programOutcome.description || 'No description available' // Use a default if description is missing
      }));
    }
  }

  // Getter for filtered competencies based on search term
  get filteredCompetencies(): TextCompetency[] {
    if (!this.wordSearch || this.wordSearch.trim() === '') {
      return this.textCompetencies;
    }
    
    const searchTerm = this.wordSearch.toLowerCase().trim();
    
    return this.textCompetencies.filter(comp => 
      comp.title.toLowerCase().includes(searchTerm) || 
      comp.outcomeDescription.toLowerCase().includes(searchTerm)
    );
  }

  onSearch(value: string): void {
    this.wordSearch = value;
    console.log('Searching for:', value);
    console.log('Filtered results:', this.filteredCompetencies.length);
  }
  
  // Updated to emit event to parent component
  onCreateCompetency(): void {
    console.log('Create competency event emitted');
    this.createCompetencyEvent.emit();
  }
  
  // Updated to emit event with index to parent component
  onDeleteCompetency(index: number): void {
    console.log('Delete competency event emitted for index:', index);
    
    // Get the actual index from the original array, not the filtered one
    const competencyToDelete = this.filteredCompetencies[index];
    const originalIndex = this.textCompetencies.findIndex(comp => 
      comp.title === competencyToDelete.title && 
      comp.outcomeDescription === competencyToDelete.outcomeDescription
    );
    
    if (originalIndex !== -1) {
      // Emit the original index so the parent can delete the correct item
      this.deleteCompetencyEvent.emit(originalIndex);
    } else {
      console.error('Could not find competency in original array');
    }
  }
}
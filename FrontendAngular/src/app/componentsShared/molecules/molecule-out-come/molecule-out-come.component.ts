import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-molecule-out-come',
  imports: [CommonModule, FormsModule],
  templateUrl: './molecule-out-come.component.html',
  styleUrl: './molecule-out-come.component.css'
})
export class MoleculeOutComeComponent {
  @Input() outcome!: string;
  @Input() outcomeNumber?: number;
  @Input() isEditable?: boolean = false;
  @Output() outcomeChange = new EventEmitter<string>();

  title: string = '';
  description: string = '';
  isEditMode: boolean = false;
  editedDescription: string = '';

  ngOnInit(): void {
    if(!this.outcomeNumber) {
      this.title = 'RA';
    }
    else{
      this.title = 'RA' + this.outcomeNumber;
    }

    this.description = this.outcome;
    this.editedDescription = this.outcome;

    this.isEditMode = this.isEditable ? true : false;
  }



  toggleEditMode(): void {
    if (this.isEditable) {
      this.isEditMode = !this.isEditMode;
      if (!this.isEditMode) {
        this.saveChanges();
      }
    }
  }

  saveChanges(): void {
    this.description = this.editedDescription;
    this.outcomeChange.emit(this.editedDescription);
  }

  cancelEdit(): void {
    this.editedDescription = this.description;
    this.isEditMode = false;
  }
}
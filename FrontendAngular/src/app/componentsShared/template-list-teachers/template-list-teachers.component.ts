import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MoleculeBlockUserComponent } from '../molecule-block-user/molecule-block-user.component';

@Component({
  selector: 'app-template-list-teachers',
  imports: [CommonModule, MoleculeBlockUserComponent],
  templateUrl: './template-list-teachers.component.html',
  styleUrl: './template-list-teachers.component.css'
})
export class TemplateListTeachersComponent {
  
}

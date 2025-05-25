import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecule-back-header/molecule-back-header.component';

@Component({
  selector: 'app-settings-teachers',
  imports: [CommonModule, MoleculeBackHeaderComponent],
  templateUrl: './settings-teachers.component.html',
  styleUrl: './settings-teachers.component.css'
})
export class SettingsTeachersComponent {

}

import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MoleculeBlockComponent } from '../../componentsShared/molecule-block/molecule-block.component';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecule-back-header/molecule-back-header.component';

@Component({
  selector: 'app-settings',
  imports: [CommonModule, MoleculeBlockComponent, MoleculeBackHeaderComponent],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent {

}

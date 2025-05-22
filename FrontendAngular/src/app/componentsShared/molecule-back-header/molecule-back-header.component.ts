import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Location } from '@angular/common';


@Component({
    selector: 'app-molecule-back-header',
    imports: [CommonModule],
    templateUrl: './molecule-back-header.component.html',
    styleUrl: './molecule-back-header.component.css'
})
export class MoleculeBackHeaderComponent {
  constructor(private location: Location) {}

  goBack(): void {
    this.location.back();
  }
}

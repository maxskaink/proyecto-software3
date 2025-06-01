import { CommonModule } from '@angular/common';
import { Component , Input} from '@angular/core';
import { Location } from '@angular/common';


@Component({
    selector: 'app-molecule-back-header',
    imports: [CommonModule],
    templateUrl: './molecule-back-header.component.html',
    styleUrl: './molecule-back-header.component.css'
})
export class MoleculeBackHeaderComponent {
  constructor(private location: Location) {}
  @Input() variant: 'primary' | 'secondary' = 'primary';
  goBack(): void {
    this.location.back();
  }
}

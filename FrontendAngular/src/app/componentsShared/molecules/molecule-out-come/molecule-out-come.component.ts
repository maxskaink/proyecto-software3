import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-molecule-out-come',
  imports: [CommonModule],
  templateUrl: './molecule-out-come.component.html',
  styleUrl: './molecule-out-come.component.css'
})
export class MoleculeOutComeComponent {
  @Input() outcome!: string;
  @Input() outcomeNumber?: number ;

  title: string = '';
  description: string = '';

  ngOnInit(): void {
    if(!this.outcomeNumber) {
      this.title = 'RA';
    }
    else{
      this.title = 'RA' + this.outcomeNumber;
    }

    this.description = this.description;
  }
}

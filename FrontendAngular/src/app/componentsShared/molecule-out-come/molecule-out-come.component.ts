import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { OutCome } from '../../models/OutComeDTO';

@Component({
  selector: 'app-molecule-out-come',
  imports: [CommonModule],
  templateUrl: './molecule-out-come.component.html',
  styleUrl: './molecule-out-come.component.css'
})
export class MoleculeOutComeComponent {
  @Input() outCome!: OutCome;

  title: string = '';
  description: string = '';

  ngOnInit(): void {
    this.title = `RA${this.outCome.id}`;
    this.description = this.outCome.description;
  }
}

import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { SubjectOutcome } from '../../models/SubjectOutcomeDTO';


@Component({
  selector: 'app-molecule-out-come',
  imports: [CommonModule],
  templateUrl: './molecule-out-come.component.html',
  styleUrl: './molecule-out-come.component.css'
})
export class MoleculeOutComeComponent {
  @Input() outcome!: SubjectOutcome;

  title: string = '';
  description: string = '';

  ngOnInit(): void {
    this.title = `RA${this.outcome.id}`;
    this.description = this.outcome.description;
  }
}

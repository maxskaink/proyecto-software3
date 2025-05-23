import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { CompetencyService } from '../../services/competency.service';
import { competencyDTO } from '../../models/CompetencyDTO';

@Component({
    selector: 'app-moleculue-competency-section',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './moleculue-competency-section.component.html',
    styleUrl: './moleculue-competency-section.component.css'
})
export class MoleculueCompetencySectionComponent {
  @Input() competency!: competencyDTO;  // <-- Aquí recibes el ID de la asignatura

  title: string = 'Title competency';
  description: string = 'Descripcion competency...';
  level: string ='';
  programCompetency: string = '';
  listCompetency: competencyDTO[] =[]
  constructor(private competencyService: CompetencyService) {}

  ngOnInit(): void {
    this.description= this.competency.descripcion;
    this.level = this.competency.level
  }
}

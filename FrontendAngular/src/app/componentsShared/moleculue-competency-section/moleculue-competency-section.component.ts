import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MoleculeOutComeComponent } from '../molecule-out-come/molecule-out-come.component';

import { SubjectOutomeService } from '../../services/subject_outcome.service';
import { ProgramCompetencyService } from '../../services/program-competency.service';
import { SubjectCompetency } from '../../models/SubjectCompetency';
import { SubjectOutcome } from '../../models/SubjectOutcome';

@Component({
    selector: 'app-moleculue-competency-section',
    standalone: true,
    imports: [CommonModule, MoleculeOutComeComponent],
    templateUrl: './moleculue-competency-section.component.html',
    styleUrl: './moleculue-competency-section.component.css'
})
export class MoleculueCompetencySectionComponent {
  @Input() competency!: SubjectCompetency;  // <-- Aquí recibes el ID de la asignatura

  title: string = 'Title ';
  description: string = 'Descripcion competency...';
  level: string ='';
  programCompetency: string = '';
  listCompetency: SubjectCompetency[] =[]
   //TODO CAMBIAR AL DTO
  listOutComes: SubjectOutcome[] = [];
 
  constructor(private outcomeService: SubjectOutomeService, private competencyProgramService: ProgramCompetencyService) {}

  ngOnInit(): void {
    this.title = this.competency.description
    this.description= this.competency.description;
    this.level = this.competency.level
    this.loadOutComes();
    this.loadInfoProgramComptency();
  }
  //TODO DECALARAR ESTO BIEN SOLO SON DATOS DE PRUEBA
  loadOutComes(): void{
    this.outcomeService.getOutcomesByCompetency(this.competency.id).subscribe(
      (listOutcomes) => {
        this.listOutComes = listOutcomes;
      }
    );
  }

  loadInfoProgramComptency() {
    this.competencyProgramService.getById(this.competency.programCompetencyId).subscribe(
      (competency) => {
        this.programCompetency = competency.description
      }
    );
  }

}

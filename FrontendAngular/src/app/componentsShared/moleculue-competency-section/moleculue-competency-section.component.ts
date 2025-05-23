import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { CompetencyService } from '../../services/competency.service';
import { competencyDTO } from '../../models/CompetencyDTO';
import { OutCome } from '../../models/SubjectOutcomeDTO';
import { MoleculeOutComeComponent } from '../molecule-out-come/molecule-out-come.component';

@Component({
    selector: 'app-moleculue-competency-section',
    standalone: true,
    imports: [CommonModule, MoleculeOutComeComponent],
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
   //TODO CAMBIAR AL DTO
  listOutComes: any = [];
 
  constructor(private competencyService: CompetencyService) {}

  ngOnInit(): void {
    this.description= this.competency.descripcion;
    this.level = this.competency.level
    this.loadOutComes();
  }
  //TODO DECALARAR ESTO BIEN SOLO SON DATOS DE PRUEBA
  loadOutComes(): void{
    this.listOutComes = [
      {id: 1, description: "Descripcion RA 1"} as OutCome,
      {id: 1, description: "Descripcion RA 2"} as OutCome, 
      {id: 1, description: "DescricpionRA 3",} as OutCome
    ]
  }
}

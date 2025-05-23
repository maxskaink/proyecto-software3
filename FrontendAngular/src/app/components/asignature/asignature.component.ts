import { Component } from '@angular/core';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecule-back-header/molecule-back-header.component';
import { ActivatedRoute } from '@angular/router';
import { AsignatureDTO } from '../../models/SubjectDTO';
import { AsignatureService } from '../../services/asignature.service';
import { MoleculeBlockUserComponent } from '../../componentsShared/molecule-block-user/molecule-block-user.component';
import { CommonModule } from '@angular/common';
import { MoleculueCompetencySectionComponent } from '../../componentsShared/moleculue-competency-section/moleculue-competency-section.component';
import { competencyDTO } from '../../models/CompetencyDTO';
import { strict } from 'assert';

@Component({
    selector: 'app-asignature',
    imports: [MoleculeBackHeaderComponent,
        MoleculeBlockUserComponent,
        MoleculueCompetencySectionComponent,
        CommonModule],
    templateUrl: './asignature.component.html',
    styleUrl: './asignature.component.css'
})
export class AsignatureComponent {
  description: string = 'description';
  title: string= 'title';
  actualAsignature: AsignatureDTO | null = null;
  listCompetency: competencyDTO[] = []; 
 
  constructor(
    private asignatureService: AsignatureService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.getAsignatureID(+id);
      this.loadCompetencies();
    }
  }

  getAsignatureID(id: number): void {
    this.asignatureService.getAsignatureID(id).subscribe(data => {
      this.actualAsignature = data;
      this.title = this.actualAsignature.title;
      this.description  = this.actualAsignature.description;
    });
  }

  loadCompetencies(): void{
    this.listCompetency  = [
      {  id: 1, descripcion: 'Descripcion generica1',level: 'Level', programCompetencyId: 2}, 
      {  id: 2, descripcion: 'Descripcion generica2',level: 'Level', programCompetencyId: 2},
      {  id: 3, descripcion: 'Descripcion generica3',level: 'Level', programCompetencyId: 2}] 
  }

/**
  loadCompetencies(): void {
    this.competencyService.getCompetenciesByAsignature(this.asignatureId)
      .subscribe({
        next: (competencies: competencyDTO[]) => {
          this.listCompetency = competencies;
        },
        error: (err: any) => {
          console.error('Error cargando competencias:', err);
        }
      });
  }
 */
}

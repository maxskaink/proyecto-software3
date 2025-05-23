import { Component } from '@angular/core';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecule-back-header/molecule-back-header.component';
import { ActivatedRoute } from '@angular/router';
import { SubjectDTO } from '../../models/SubjectDTO';
import { AsignatureService } from '../../services/subject.service';
import { MoleculeBlockUserComponent } from '../../componentsShared/molecule-block-user/molecule-block-user.component';
import { CommonModule } from '@angular/common';
import { MoleculueCompetencySectionComponent } from '../../componentsShared/moleculue-competency-section/moleculue-competency-section.component';
import { SubjectCompetencyService } from '../../services/subject_competency.service';
import { SubjectCompetency } from '../../models/SubjectDTO';

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
  actualAsignature: SubjectDTO | null = null;
  listCompetency: SubjectCompetency[] = []; 
  id: number = -1;
  constructor(
    private asignatureService: AsignatureService,
    private route: ActivatedRoute,
    private competenciesSubject: SubjectCompetencyService 
  ) {}

  ngOnInit() {
    const response = this.route.snapshot.paramMap.get('id');
    if(!response)
      console.log("The subject doesnt exist");
    else {
      this.id = Number(response);
      if (this.id) {
        this.getAsignatureID(+this.id);
        this.loadCompetencies();
      }
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
    this.competenciesSubject.getCompetenciesByAsignature(this.id).subscribe(
      (listCompetencies) => {
        this.listCompetency = listCompetencies;
      }
    );
  
  }

}

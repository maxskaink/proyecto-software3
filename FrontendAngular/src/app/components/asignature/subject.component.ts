import { Component } from '@angular/core';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecules/molecule-back-header/molecule-back-header.component';
import { ActivatedRoute } from '@angular/router';
import { SubjectDTO } from '../../models/SubjectDTO';
import { SubjectService } from '../../services/subject.service';
import { MoleculeBlockUserComponent } from '../../componentsShared/molecules/molecule-block-user/molecule-block-user.component';
import { CommonModule } from '@angular/common';
import { SubjectCompetencyService } from '../../services/subject_competency.service';
import { SubjectCompetency } from '../../models/SubjectCompetencyDTO';
import { TemplateCompetencyComponent } from '../../componentsShared/templates/template-competency/template-competency.component';
import { TemplateCompetencyEditComponent } from '../../componentsShared/templates/template-competency-edit/template-competency-edit.component';


@Component({
  selector: 'app-asignature',
  imports: [
      MoleculeBackHeaderComponent,
      MoleculeBlockUserComponent,
      TemplateCompetencyComponent, // Fixed name
      TemplateCompetencyEditComponent,
      CommonModule,

  ],
  templateUrl: './subject.component.html',
  styleUrl: './subject.component.css'
})
export class SubjectComponent {
  description: string = 'description';
  title: string= 'title';
  actualAsignature: SubjectDTO | null = null;
  listCompetency: SubjectCompetency[] = []; 
  id: number = -1;
  isEdit: boolean = false;
  constructor(
    private asignatureService: SubjectService,
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
    console.log(this.title)
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
  onEditStateChange(newState: boolean): void {
    this.isEdit = newState;
  }
  
}

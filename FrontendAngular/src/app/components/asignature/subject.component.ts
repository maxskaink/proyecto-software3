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
import { AuthService } from '../../services/auth.service';


@Component({
  selector: 'app-asignature',
  imports: [
      MoleculeBackHeaderComponent,
      MoleculeBlockUserComponent,
      TemplateCompetencyComponent, // Fixed name
      CommonModule
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

  role: string ="";
  name: string ="";
  constructor(
    private asignatureService: SubjectService,
    private route: ActivatedRoute,
    private competenciesSubject: SubjectCompetencyService ,
    private auth: AuthService
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

    this.auth.role.subscribe(role=> {
      if(role!=null)
        this.role=role;
    });
    this.auth.name.subscribe(name=>{
      if(name!=null)
        this.name=name;
    })
  }

  getAsignatureID(id: number): void {
    this.asignatureService.getSubjectID(id).subscribe(data => {
      this.actualAsignature = data;
      this.title = this.actualAsignature.name;
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

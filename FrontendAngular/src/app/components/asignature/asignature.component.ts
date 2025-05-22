import { Component } from '@angular/core';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecule-back-header/molecule-back-header.component';
import { ActivatedRoute } from '@angular/router';
import { AsignatureDTO } from '../../models/AsignatureDTO';
import { AsignatureService } from '../../services/asignature.service';
import { MoleculeBlockUserComponent } from '../../componentsShared/molecule-block-user/molecule-block-user.component';
import { CommonModule } from '@angular/common';
import { MoleculueCompetencySectionComponent } from '../../componentsShared/moleculue-competency-section/moleculue-competency-section.component';

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
  competencies: any; 
 
  constructor(
    private asignatureService: AsignatureService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.getAsignatureID(+id);
    }
  }

  getAsignatureID(id: number): void {
    this.asignatureService.getAsignatureID(id).subscribe(data => {
      this.actualAsignature = data;
      this.title = this.actualAsignature.title;
      this.description  = this.actualAsignature.description;
    });
  }
}

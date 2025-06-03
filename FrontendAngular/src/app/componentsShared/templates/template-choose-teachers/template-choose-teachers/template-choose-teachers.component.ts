import { Component, Input } from '@angular/core';
import { TemplateListTeachersComponent } from '../../template-list-teachers/template-list-teachers.component';
import { MoleculeBlockUserComponent } from '../../../molecules/molecule-block-user/molecule-block-user.component';
import { TeacherDTO } from '../../../../models/TeacherDTO';



@Component({
  selector: 'app-template-choose-teachers',
  imports: [TemplateListTeachersComponent, MoleculeBlockUserComponent],
  templateUrl: './template-choose-teachers.component.html',
  styleUrl: './template-choose-teachers.component.css'
})
export class TemplateChooseTeachersComponent {
  @Input() listTeachers: TeacherDTO[] = [];

  constructor(){}
}

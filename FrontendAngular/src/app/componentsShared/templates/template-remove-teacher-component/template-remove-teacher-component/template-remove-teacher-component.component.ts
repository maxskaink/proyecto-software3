import { Component, EventEmitter, Input, Output, OnChanges, SimpleChanges } from '@angular/core';
import { MoleculeBlockUserComponent } from '../../../molecules/molecule-block-user/molecule-block-user.component';
import { TeacherDTO } from '../../../../models/TeacherDTO';
import { CommonModule } from '@angular/common';
import { BehaviorSubject } from 'rxjs';
import { TeacherAssignmentService } from '../../../../services/teacher_assignment.service';

@Component({
  selector: 'app-template-remove-teacher',
  imports: [
    CommonModule,
    MoleculeBlockUserComponent,
  ],
  templateUrl: './template-remove-teacher-component.component.html',
  styleUrl: './template-remove-teacher-component.component.css'
})
export class TemplateRemoveTeacherComponentComponent implements OnChanges {
  @Input() selectedTeachers: TeacherDTO[] = [];
  @Input() originalTeachers: TeacherDTO[] = [];
  @Output() teacherRemoved = new EventEmitter<TeacherDTO>();

  private teachersSubject = new BehaviorSubject<TeacherDTO[]>([]);
  teachers$ = this.teachersSubject.asObservable();

  constructor(private teacherAssignmentService: TeacherAssignmentService) {
  }


  ngOnChanges(changes: SimpleChanges): void {
    if (changes['selectedTeachers']) {
      this.teachersSubject.next([...(this.selectedTeachers || [])]);
    }
  }

  removeTeacher(teacher: TeacherDTO): void {
    this.teacherRemoved.emit(teacher);
  }


  saveAssignedTeachers(): void {

  }
}

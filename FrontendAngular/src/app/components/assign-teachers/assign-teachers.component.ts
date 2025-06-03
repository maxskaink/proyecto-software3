import {
  Component,
  viewChild,
  ElementRef,
  ViewChild,
  EventEmitter,
  Inject,
  PLATFORM_ID,
  OnInit,
  AfterViewInit,
} from '@angular/core';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecules/molecule-back-header/molecule-back-header.component';
import { ActivatedRoute, Router } from '@angular/router';
import { SubjectDTO } from '../../models/SubjectDTO';
import { SubjectService } from '../../services/subject.service';
import { MoleculeBlockUserComponent } from '../../componentsShared/molecules/molecule-block-user/molecule-block-user.component';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { SubjectCompetencyService } from '../../services/subject_competency.service';
import { SubjectCompetency } from '../../models/SubjectCompetencyDTO';
import { TemplateCompetencyComponent } from '../../componentsShared/templates/template-competency/template-competency.component';
import { TemplateCompetencyEditComponent } from '../../componentsShared/templates/template-competency-edit/template-competency-edit.component';
import { AuthService } from '../../services/auth.service';
import { EditStateService } from '../../services/edit-state.service';
import { Carousel } from 'bootstrap';
import { LoadingComponent } from '../../componentsShared/loading/loading.component';
import { forkJoin, Observable } from 'rxjs';
import { TemplateListTeachersComponent } from '../../componentsShared/templates/template-list-teachers/template-list-teachers.component';
import { TeacherAssignmentService } from '../../services/teacher_assignment.service';
import { TeacherAssignment } from '../../models/TeacherAssignmentDTO';
import { TeacherDTO } from '../../models/TeacherDTO';
import { TemplateChooseTeachersComponent } from '../../componentsShared/templates/template-choose-teachers/template-choose-teachers/template-choose-teachers.component';


@Component({
  selector: 'app-assign-teachers',
  imports: [CommonModule, LoadingComponent, MoleculeBackHeaderComponent, TemplateChooseTeachersComponent],
  templateUrl: './assign-teachers.component.html',
  styleUrl: './assign-teachers.component.css',
})
export class AssignTeachersComponent {
  isLoading: boolean = false;
  subjectId: number = -1;
  teachers: TeacherDTO[] = [];

  constructor(private route: ActivatedRoute,     private auth: AuthService) {}
  ngOnInit() {
    this.isLoading = true;
    const response = this.route.snapshot.paramMap.get('id');
    if (!response) {
      console.error("The subject doesn't exist");
      this.isLoading = false;
      return;
    }

    this.subjectId = Number(response);
    this.loadInitialData();
  }

  loadInitialData(): void {
    this.auth.getAllUsers().subscribe({
      next: (teachers: TeacherDTO[]) => {
        this.teachers = teachers;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading teachers:', err);
        this.isLoading = false;
      }
    });
  }
}

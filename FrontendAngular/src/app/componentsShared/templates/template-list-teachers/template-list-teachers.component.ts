import { CommonModule } from '@angular/common';
import {Component, Input, OnInit} from '@angular/core';
import { MoleculeBlockUserComponent } from '../../molecules/molecule-block-user/molecule-block-user.component';
import { TeacherDTO } from '../../../models/TeacherDTO';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-template-list-teachers',
  imports: [CommonModule, MoleculeBlockUserComponent],
  templateUrl: './template-list-teachers.component.html',
  styleUrl: './template-list-teachers.component.css'
})
export class TemplateListTeachersComponent implements OnInit{
  @Input() title?: string;
  @Input() description?: string;
  @Input() listTeachers?: TeacherDTO[] = [];
  @Input() forceTeacherRole?: boolean = false;
  @Input() editable: boolean = false;
  
  constructor(private authService: AuthService) {}
  
  hoveredStates: { [key: string]: boolean } = {};

  onUserHover(userId: string, isHovered: boolean) {
    this.hoveredStates[userId] = isHovered;
  }

  ngOnInit(): void {
    if(!this.listTeachers)
      this.getUsers();
  }

  async getUsers(): Promise<void> {
    this.authService.getAllUsers().subscribe({
      next: (users) => {
        this.listTeachers = users;
      },
      error: (error) => {
        console.error('Error retrieving the users:', error);
      }
    });
  }

  getFullName(teacher: TeacherDTO): string {
    return `${teacher.name} ${teacher.lastName}`;
  }
}

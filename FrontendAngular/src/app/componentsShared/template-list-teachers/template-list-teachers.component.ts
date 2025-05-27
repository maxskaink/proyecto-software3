import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MoleculeBlockUserComponent } from '../molecule-block-user/molecule-block-user.component';
import { TeacherDTO } from '../../models/TeacherDTO';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-template-list-teachers',
  imports: [CommonModule, MoleculeBlockUserComponent],
  templateUrl: './template-list-teachers.component.html',
  styleUrl: './template-list-teachers.component.css'
})
export class TemplateListTeachersComponent {
  @Input() title?: string;
  @Input() description?: string;
  listTeacherDTO: TeacherDTO[] = [];

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.getUsers();
  }

  async getUsers(): Promise<void> {
    try {
      this.listTeacherDTO = await this.authService.getAllUsers();
    } catch (error) {
      console.error('Error retrieving the users:', error);
    }
  }
}
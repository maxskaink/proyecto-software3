import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MoleculeBackHeaderComponent } from "../../componentsShared/molecules/molecule-back-header/molecule-back-header.component";
import { AuthService } from '../../services/auth.service';
import { TeacherDTO } from '../../models/TeacherDTO';
import { Location } from '@angular/common';

@Component({
  selector: 'app-teacher-create',
  standalone: true,
  imports: [MoleculeBackHeaderComponent, ReactiveFormsModule, CommonModule],
  templateUrl: './teacher-create.component.html',
  styleUrl: './teacher-create.component.css'
})
export class TeacherCreateComponent implements OnInit {
  protected update: boolean = false;
  protected create: boolean = true;
  title = "";
  description = "";
  idUpdate: string = "";
  teacherForm!: FormGroup;
  isSubmitting = false;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private location: Location
  ) {
    this.createForm();
  }

  ngOnInit() {
    const response = this.route.snapshot.paramMap.get('id');
    if (response != null) {
      this.update = true;
      this.create = false;
      this.idUpdate = response;
      this.loadTeacherData();
    }

    this.title = this.create ? "Crea un docente" : "Editar docente";
    this.description = this.create ? "En esta sección podrás crear un docente" : "En esta sección podrás editar un docente";
  }


  createForm() {
    this.teacherForm = this.fb.group({
      name: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      identification: ['', Validators.required],
      identificationType: ['', Validators.required],
      academicTitle: ['', Validators.required],
      role: ['', Validators.required],
      typeTeacher: ['', Validators.required]
    });
  }

  loadTeacherData() {
    this.authService.getUserById(this.idUpdate).subscribe({
      next: (teacher) => {
        console.log(teacher)
        this.teacherForm.patchValue({
          name: teacher.name,
          lastName: teacher.lastName,
          email: teacher.email,
          identification: teacher.identification,
          identificationType: teacher.identificationType,
          academicTitle: teacher.academicTitle,
          role: teacher.role,
          typeTeacher: teacher.typeTeacher
        });
      },
      error: (error) => {
        console.error('Error cargando datos del docente:', error);
      }
    });
  }

  // Getters para simplificar el acceso en la plantilla
  get name() { return this.teacherForm.get('name'); }
  get lastName() { return this.teacherForm.get('lastName'); }
  get email() { return this.teacherForm.get('email'); }
  get identification() { return this.teacherForm.get('identification'); }
  get identificationType() { return this.teacherForm.get('identificationType'); }
  get academicTitle() { return this.teacherForm.get('academicTitle'); }
  get role() { return this.teacherForm.get('role'); }
  get typeTeacher() { return this.teacherForm.get('typeTeacher'); }


  onSubmit() {
    if (this.teacherForm.invalid) return;

    this.isSubmitting = true;
    const teacherData: TeacherDTO = this.teacherForm.value;

    if (this.create) {
      this.authService.createUser(teacherData).subscribe({
        next: () => {
          this.router.navigate(['/settings/teacher']);
        },
        error: (error) => {
          console.error('Error al crear docente:', error);
          this.isSubmitting = false;
        }
      });
    } else {
      teacherData.id = this.idUpdate;
      this.authService.updateUser(teacherData).subscribe({
        next: () => {
          this.router.navigate(['/settings/teacher']);
        },
        error: (error) => {
          console.error('Error al actualizar docente:', error);
          this.isSubmitting = false;
        }
      });
    }
  }

  goBack() {
    this.location.back();
  }

}

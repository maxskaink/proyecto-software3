import {CommonModule, ViewportScroller} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import { MoleculeBackHeaderComponent } from '../../componentsShared/molecules/molecule-back-header/molecule-back-header.component';
import { TemplateListboxCompleteComponent, TextBlock } from '../../componentsShared/templates/template-listbox-complete/template-listbox-complete.component';
import { TemplateInputBoxtextComponent } from '../../componentsShared/templates/template-input-boxtext/template-input-boxtext.component';
import { Router } from '@angular/router';
import {TermService} from "../../services/term.service";
import {SubjectService} from "../../services/subject.service";
import {SubjectDTO} from "../../models/SubjectDTO";
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-settings-asignature',
  imports: [
    CommonModule,
    MoleculeBackHeaderComponent,
    TemplateListboxCompleteComponent,
    TemplateInputBoxtextComponent,
    FormsModule
  ],
  templateUrl: './settings-asignature.component.html',
  styleUrl: './settings-asignature.component.css'
})
export class SettingsAsignatureComponent implements OnInit{
  periodo: string = '';
  textBlock: TextBlock[] = [
    {
      title: 'Crear asignatura',
      description: 'Crea una asignatura nueva para este periodo',
      route: ''
    },
    {
      title: 'Reutlizar asignatura',
      description: 'reutiliza una asignatura del periodo pasado',
      route: ''
    }
  ];

  subjectName: string = '';
  subjectNameTouched: boolean = false;


  constructor(
    private router: Router,
    private termService: TermService,
    private subjecService:SubjectService,
    private viewportScroller: ViewportScroller
  ) {}

  ngOnInit() {
    this.getActiveTerm();
  }

  handleBlockClick(block: TextBlock): void {
    if (block.title.toLowerCase().includes('crear')) {
      document.getElementById('createAsignature')?.scrollIntoView({ behavior: 'smooth' });
    } else if (block.route) {
      this.router.navigate([block.route]);
    }
  }

  handleCreateSubject(description :string){
    this.subjectNameTouched = true;

    // Validar que el nombre no esté vacío
    if (!this.subjectName.trim()) {
      return;
    }

    const newSubject: SubjectDTO = {
      id: 0,
      name: this.subjectName.trim(),
      description: description.trim()
    }

    this.subjecService.createSubject(newSubject).subscribe({
      next: (data) => {
        console.log('Asignatura creada:', data);
        this.router.navigate(['/asignatures/', data.id]).then(() => {
          this.viewportScroller.scrollToPosition([0,0]);
        });
      },
      error: (error) => {
        console.error('Error al crear asignatura:', error);
      }
      });
    }

  private getActiveTerm() {
    this.termService.getActiveTerm().subscribe({
      next:(term)=>{
        this.periodo = term.description;
      },
      error:()=>{
        console.error('Error al obtener el periodo activo');
      }
    });
  }
  onSubjectNameBlur() {
  this.subjectNameTouched = true;
  }
}

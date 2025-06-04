import { Component, Input, OnInit } from '@angular/core';
import {CommonModule} from "@angular/common";
import { Router } from '@angular/router';

@Component({
    selector: 'app-molecule-block-user',
    imports: [CommonModule],
    templateUrl: './molecule-block-user.component.html',
    styleUrl: './molecule-block-user.component.css'
})
export class MoleculeBlockUserComponent implements OnInit{
  @Input() name: string = '';
  @Input() role: string ='';
  @Input() colorHover: string = '#8D538C'
  @Input() variant: 'primary' | 'secondary' = 'primary';
  @Input() editable: boolean = false;
  @Input() uid:string |undefined='';
  @Input() forceTeacherRole?: boolean = false; // Nuevo parámetro opcional

  constructor(private router:Router){}

  ngOnInit() {
    //console.log(this.uid)
  }

  /**
   * Traduce los roles de inglés a español
   * Si forceTeacherRole es true, siempre devuelve "Docente" independientemente del rol original
   * @returns El rol traducido al español
   */
  translateRole(): string {
    // Si forceTeacherRole está activo, siempre devuelve "Docente"
    if (this.forceTeacherRole) return 'Docente';

    if (!this.role) return '';

    const normalizedRole = this.role.trim().toLowerCase();

    switch (normalizedRole) {
      case 'coordinator':
        return 'Coordinador';
      case 'teacher':
        return 'Docente';
      case 'admin':
        return 'Administrador';
      default:
        return this.role; // Si no coincide con ninguno conocido, devuelve el original
    }
  }

  onDelete() {
    console.log('Eliminar usuario');
  }

  handlerEdit(){
    this.router.navigate([`/settings/teacher/update/${this.uid}`])
  }
}

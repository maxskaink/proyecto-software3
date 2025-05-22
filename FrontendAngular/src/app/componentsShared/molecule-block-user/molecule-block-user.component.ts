import { Component } from '@angular/core';

@Component({
  selector: 'app-molecule-block-user',
  standalone: true,
  imports: [],
  templateUrl: './molecule-block-user.component.html',
  styleUrl: './molecule-block-user.component.css'
})
export class MoleculeBlockUserComponent {
  nombre: string = 'Catalan Aguado';
  rol: string ='Docente'
  
  /**
   * get info of user for see in the frontend individual
  **/
  getInfoUser(id:number){

  }
}

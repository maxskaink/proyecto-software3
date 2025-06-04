import { Component, Input, OnInit } from '@angular/core';
import {CommonModule} from "@angular/common";

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

  ngOnInit() {
    //console.log(this.editable)
  }

  onDelete() {
    console.log('Eliminar usuario');
  }
  handlerEdit(){
    console.log('Editar usuario');
  }
}

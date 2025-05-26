import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-molecule-block-user',
    imports: [],
    templateUrl: './molecule-block-user.component.html',
    styleUrl: './molecule-block-user.component.css'
})
export class MoleculeBlockUserComponent {
  @Input() name: string = '';
  @Input() rol: string ='';
}

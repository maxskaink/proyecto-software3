import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-molecule-block-user',
    imports: [],
    templateUrl: './molecule-block-user.component.html',
    styleUrl: './molecule-block-user.component.css'
})
export class MoleculeBlockUserComponent {
  @Input() name: string = '';
  @Input() role: string ='';
  @Input() colorHover: string = '#8D538C'
  @Input() variant: 'primary' | 'secondary' = 'primary';
}

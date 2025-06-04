import { CommonModule } from '@angular/common';
import { Component , Input} from '@angular/core';
import { Location } from '@angular/common';
import {MatDialog} from "@angular/material/dialog";
import {ModalConfirmComponent} from "../../messages/modal-confirm/modal-confirm.component";


@Component({
    selector: 'app-molecule-back-header',
    imports: [CommonModule],
    templateUrl: './molecule-back-header.component.html',
    styleUrl: './molecule-back-header.component.css'
})
export class MoleculeBackHeaderComponent {
  @Input() variant: 'primary' | 'secondary' = 'primary';
  @Input() confirmBack: boolean = false;

  constructor(private dialog: MatDialog, private location: Location) {  }

  goBack(): void {
    if(this.confirmBack){
      this.dialog.open(ModalConfirmComponent, {
        data: {
          message: '¿Está seguro que desea volver?'
        }
      }).afterClosed().subscribe(result => {
        if (result) {
          this.location.back();
        }
      })
    }else
      this.location.back();
  }
}

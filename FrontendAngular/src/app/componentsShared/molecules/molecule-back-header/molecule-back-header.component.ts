import { CommonModule } from '@angular/common';
import { Component , Input} from '@angular/core';
import { Location } from '@angular/common';
import {MatDialog} from "@angular/material/dialog";
import {ModalConfirmComponent} from "../../messages/modal-confirm/modal-confirm.component";
import { Router } from '@angular/router';

@Component({
    selector: 'app-molecule-back-header',
    imports: [CommonModule],
    templateUrl: './molecule-back-header.component.html',
    styleUrl: './molecule-back-header.component.css'
})
export class MoleculeBackHeaderComponent {
  @Input() variant: 'primary' | 'secondary' = 'primary';
  @Input() confirmBack: boolean = false;
  @Input() customBackRoute?: string; // Nueva propiedad para ruta personalizada
  @Input() customBackQueryParams?: any; // Parámetros de consulta opcionales

  constructor(
    private dialog: MatDialog, 
    private location: Location,
    private router: Router
  ) {}

  goBack(): void {
    if (this.confirmBack) {
      this.dialog.open(ModalConfirmComponent, {
        data: { message: '¿Está seguro que desea volver?' }
      }).afterClosed().subscribe(result => {
        if (result) {
          this.handleNavigation();
        }
      });
    } else {
      this.handleNavigation();
    }
  }

  private handleNavigation(): void {
    if (this.customBackRoute) {
      // Navegar a ruta específica si se proporciona
      this.router.navigate([this.customBackRoute], {
        queryParams: this.customBackQueryParams || {}
      });
    } else {
      // Comportamiento por defecto
      this.location.back();
    }
  }
}

import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MoleculeBackHeaderComponent } from '../../molecules/molecule-back-header/molecule-back-header.component';
import { Router } from '@angular/router';
@Component({
  selector: 'app-template-header-title',
  imports: [CommonModule, MoleculeBackHeaderComponent],
  templateUrl: './template-header-title.component.html',
  styleUrl: './template-header-title.component.css'
})
export class TemplateHeaderTitleComponent {
  @Input() title: string = '';
  @Input() description: string = '';
  @Input() variant: 'primary' | 'secondary' = 'primary';
  @Input() confirmBack: boolean = false;
  @Input() customBackRoute?: string;
  @Input() customBackQueryParams?: any;

  constructor(private router: Router) {}

  goToHome() {
    this.router.navigate(['/home']);
  }
}

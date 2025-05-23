import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'app-header',
    imports: [CommonModule],
    templateUrl: './header.component.html',
    styleUrl: './header.component.css'
})
export class HeaderComponent {
  nombre = 'Juan Pérez';
  rol = 'Administrador';

  constructor(private router: Router) {}
  goToSettings(): void {
    this.router.navigate(['/settings']);
  }
}

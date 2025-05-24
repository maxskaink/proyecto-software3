import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-header',
    imports: [CommonModule],
    templateUrl: './header.component.html',
    styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit{
  user: any;
  nombre = 'NO SE ENCONTRO';
  rol = 'Administrador';

  constructor(private router: Router, private authService: AuthService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}
  goToSettings(): void {
    this.router.navigate(['/settings']);
  }
  ngOnInit() {
    this.getUser();
  }
 
  getUser(): void {
    this.user = this.authService.currentUser;

    if (this.user) {
      this.nombre = this.user.name || 'Nombre no disponible';
      this.rol = this.user.role || 'Sin rol';
    }

    console.log('Usuario:', this.user);
  }
}

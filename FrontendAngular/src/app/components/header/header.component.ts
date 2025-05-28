import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

// Define an interface for the user data structure
interface User {
  name: string;
  role: string;
}

@Component({
    selector: 'app-header',
    imports: [CommonModule],
    templateUrl: './header.component.html',
    styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  user: User | null = null;
  name = 'NO SE ENCONTRO';
  role = 'Administrador';

  constructor(
    private router: Router, 
    private authService: AuthService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  goToSettings(): void {
    this.router.navigate(['/settings']);
  }

  ngOnInit() {
    this.getUser();
  }
 
  getUser(): void {
    this.user = this.authService.currentUser as User;

    if (this.user) {
      this.name = this.user.name || 'Nombre no disponible';
      this.name = this.user.role || 'Sin rol';
    }

    console.log('Usuario:', this.user);
  }
}
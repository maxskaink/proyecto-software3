import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { MoleculeBlockUserComponent } from '../../componentsShared/molecules/molecule-block-user/molecule-block-user.component';

// Define an interface for the user data structure
interface User {
  name: string;
  role: string;
}

@Component({
    selector: 'app-header',
    imports: [CommonModule,MoleculeBlockUserComponent],
    templateUrl: './header.component.html',
    styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  name = '';
  role = '';

  constructor(
    private router: Router, 
    private authService: AuthService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}
  headerState: 'primary' | 'secondary' = 'primary';
  
  onHeaderHover(isHovering: boolean) {
    this.headerState = isHovering ? 'secondary' : 'primary';
  }
  goToSettings(): void {
    this.router.navigate(['/settings']);
  }

  ngOnInit() {
    this.authService.role.subscribe(role => this.role = role || 'Sin rol');
    this.authService.name.subscribe(name => this.name = name || 'Nombre no disponible');
  }
  isCoordinator(): boolean {
    return this.role?.toLowerCase() === 'coordinator';
  }
 
}
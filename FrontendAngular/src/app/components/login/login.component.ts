import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  errorMsg: string = '';
  email: string = '';
  password: string = '';
  
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  public async login() {
    try {
      await this.authService.login(this.email,this.password);
      this.router.navigate(['/home']); // redirige tras login
    } catch (err) {
      this.errorMsg = 'Credenciales incorrectas o error de red.';
      console.error(err);
    }
  }
}

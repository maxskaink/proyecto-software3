import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Component, Inject, PLATFORM_ID } from '@angular/core';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule],
  template: `
    <section class="d-flex flex-column align-items-center gap-3 p-4 justify-content-center shadow boxLoguin">
      <h1>SERA</h1>
      <i class="bi bi-person-circle p-2 user-icon"></i>
      <button class="btn btn-primary custom-button container-fluid" (click)="login()">Iniciar sesión con Keycloak</button>
      <div *ngIf="errorMsg" class="alert alert-danger mt-2">{{ errorMsg }}</div>
    </section>
  `,
  styleUrl: './login.component.css'
})
export class LoginComponent {
  errorMsg: string = '';

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  public async login() {

  //TODO implementar
  }
}

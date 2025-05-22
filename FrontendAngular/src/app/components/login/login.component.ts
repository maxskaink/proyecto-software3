import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { OAuthModule, OAuthService, provideOAuthClient, UrlHelperService } from 'angular-oauth2-oidc';
import { authCodeFlowConfig } from '../../app.config';
import { Router } from '@angular/router';

@Component({
    selector: 'app-login',
    imports: [CommonModule],
    templateUrl: './login.component.html',
    styleUrl: './login.component.css'
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  errorMsg: string = '';

  constructor(
    private oauthService: OAuthService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.oauthService.configure(authCodeFlowConfig);
    if (isPlatformBrowser(this.platformId)) {
      this.oauthService.setStorage(sessionStorage);
    }
  
  }
   
  
  
  async login() {
    try {
      // Configurá el servicio con tu cliente antes de autenticar
      this.oauthService.configure(authCodeFlowConfig);
  
      await this.oauthService.loadDiscoveryDocumentAndTryLogin();
  
      await this.oauthService.fetchTokenUsingPasswordFlowAndLoadUserProfile(
        this.username,
        this.password
      );
  
      if (this.oauthService.hasValidAccessToken()) {
        this.router.navigate(['/home']);
      }
    } catch (error) {
      console.error('Error de login', error);
      this.errorMsg = 'Usuario o contraseña incorrectos';
    }
  }
  getRoles(): string[] {
    const claims = this.oauthService.getIdentityClaims() as any;
    return claims?.realm_access?.roles || [];
  }
}
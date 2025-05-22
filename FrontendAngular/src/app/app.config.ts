import { APP_INITIALIZER, ApplicationConfig, inject, PLATFORM_ID } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { AuthConfig, OAuthModule, OAuthService, provideOAuthClient } from 'angular-oauth2-oidc';
import { resolve } from 'node:path';
import { isPlatformBrowser } from '@angular/common';

export const authCodeFlowConfig: AuthConfig = {
  issuer: 'http://localhost:8080/realms/SERA',
  clientId: 'angularSERA-client',
  scope: 'openid profile email',
  strictDiscoveryDocumentValidation: false,
  requireHttps: false // Permitir HTTP en localhost
};


function initializeOAuth(oauthService: OAuthService, platformId: Object): () => Promise<void | boolean> {
  return () => {
    // Only run OAuth initialization in the browser environment
    if (isPlatformBrowser(platformId)) {
      // Configure redirectUri dynamically
      authCodeFlowConfig.redirectUri = window.location.origin;
      
      oauthService.configure(authCodeFlowConfig);
      oauthService.setupAutomaticSilentRefresh();
      
      // Solo cargar el documento de descubrimiento sin iniciar login automático
      return oauthService.loadDiscoveryDocumentAndTryLogin();
        
    }
    
    // Return resolved promise for server-side rendering
    return Promise.resolve();
  };
}

export const appConfig: ApplicationConfig = { 
  providers: [
    provideRouter(routes),
    provideClientHydration(),
    provideHttpClient(),
    provideOAuthClient(),
    {
      provide: APP_INITIALIZER,
      useFactory: (oauthService: OAuthService) => {
        const platformId = inject(PLATFORM_ID);
        return initializeOAuth(oauthService, platformId);
      },
      multi: true,
      deps: [OAuthService]
    }
  ]
};

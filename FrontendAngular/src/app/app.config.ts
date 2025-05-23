import { APP_INITIALIZER, ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakInitService } from './services/keycloak.service';
import { keycloakInterceptor } from './app.keycloak';

function initializeKeycloak(keycloakInitService: KeycloakInitService) {
  return () => keycloakInitService.init();
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideClientHydration(),
    provideHttpClient(withInterceptors([keycloakInterceptor])),
    KeycloakService,
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakInitService]
    }
  ]
};



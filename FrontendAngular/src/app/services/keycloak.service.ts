import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { KeycloakService } from 'keycloak-angular';
import { keycloakConfig } from "../app.keycloak";

@Injectable({ providedIn: 'root' })
export class KeycloakInitService {
  constructor(
    private keycloak: KeycloakService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  async init(): Promise<boolean> {
    if (!isPlatformBrowser(this.platformId)) {
      // No inicializar Keycloak en el servidor
      return false;
    }
    try {
      return await this.keycloak.init({
        config: keycloakConfig,
        initOptions: {
          onLoad: 'check-sso',
          checkLoginIframe: false,
          // Elimina silentCheckSsoRedirectUri que causa error
        },
        enableBearerInterceptor: false,
        bearerExcludedUrls: ['/assets']
      });
    } catch (error) {
      console.error('Error inicializando Keycloak', error);
      return false;
    }
  }
}

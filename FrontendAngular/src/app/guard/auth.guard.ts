import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser, isPlatformServer } from '@angular/common';
import {
  ActivatedRouteSnapshot,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import { KeycloakAuthGuard, KeycloakService } from 'keycloak-angular';

@Injectable({ providedIn: 'root' })
export class AuthGuard extends KeycloakAuthGuard {
  constructor(
    protected override readonly router: Router,
    protected readonly keycloak: KeycloakService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    super(router, keycloak);
  }

  public async isAccessAllowed(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Promise<boolean | UrlTree> {
    if (isPlatformServer(this.platformId)) return true;

    if (!this.authenticated) {
      await this.keycloak.login({ redirectUri: window.location.origin + state.url });
      return false;
    }

    const tokenParsed = this.keycloak.getKeycloakInstance().tokenParsed;
    const resourceAccess = tokenParsed?.resource_access;
    const clientRoles = (resourceAccess as any)?.['angularSERA-client']?.roles ?? [];
    const requiredRoles = route.data['roles'];

    if (!requiredRoles?.length) return true;

    const hasRole = requiredRoles.some((role: string) => clientRoles.includes(role));
    return hasRole ? true : this.router.parseUrl('/');
  }
}

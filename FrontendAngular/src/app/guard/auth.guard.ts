import { CanActivateFn, ActivatedRouteSnapshot } from '@angular/router';
import { inject } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const oauthService = inject(OAuthService);
  const claims: any = oauthService.getIdentityClaims();
  const roles = claims?.realm_access?.roles || [];

  const allowedRoles = route.data['roles'] as string[];

  // Permite acceso si el usuario tiene al menos uno de los roles requeridos
  return allowedRoles.some(role => roles.includes(role));
};
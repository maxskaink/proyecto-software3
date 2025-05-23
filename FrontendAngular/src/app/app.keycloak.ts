import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { from, switchMap, of } from 'rxjs';

export const keycloakConfig = {
  url: 'http://localhost:8080',
  realm: 'SERA',
  clientId: 'angularSERA-client'
};

export const keycloakInterceptor: HttpInterceptorFn = (req, next) => {
  const keycloakService = inject(KeycloakService);

  return from(Promise.resolve(keycloakService.isLoggedIn())).pipe(
    switchMap(isLoggedIn => {
      if (isLoggedIn) {
        return from(keycloakService.getToken()).pipe(
          switchMap(token => {
            if (token) {
              const authReq = req.clone({
                setHeaders: {
                  Authorization: `Bearer ${token}`
                }
              });
              return next(authReq);
            }
            return next(req);
          })
        );
      }
      return next(req);
    })
  );
};

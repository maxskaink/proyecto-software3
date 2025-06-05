import { Injectable } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { map, take } from 'rxjs/operators';

export function RoleGuard(allowedRoles: string[]): CanActivateFn {
  return () => {
    const router = inject(Router);
    const authService = inject(AuthService);

    return authService.role.pipe(
      take(1),
      map(role => {
        const normalizedRole = role?.toLowerCase();
        const normalizedAllowedRoles = allowedRoles.map(r => r.toLowerCase());
        
        if (normalizedRole && normalizedAllowedRoles.includes(normalizedRole)) {
          return true;
        }
        
        router.navigate(['/access-denied']);
        return false;
      })
    );
  };
}
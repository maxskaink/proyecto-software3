import { Injectable } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service'; 
import { map, take } from 'rxjs/operators';

export function RoleGuard(allowedRoles: string[]): CanActivateFn {
  return () => {
    const router = inject(Router);
    const role = localStorage.getItem('userRole');

    if (role && allowedRoles.includes(role)) {
      return true;
    } else {
      router.navigate(['/no-autorizado']);
      return false;
    }
  };
}

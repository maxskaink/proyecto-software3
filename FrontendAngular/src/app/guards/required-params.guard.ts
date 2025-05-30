import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RequiredParamsGuard {
  
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> | Promise<boolean> | boolean {
    // Check for required query parameters
    const subjectId = route.queryParamMap.get('subjectId');
    if (!subjectId ) {
      console.error('Missing required parameters: subjectId and programCompetencyId are required');
      
      // Just prevent navigation without redirecting anywhere
      // This will keep the user at their current location
      return false;
    }
    
    return true;
  }
}
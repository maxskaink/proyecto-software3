import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ErrorResponse } from '../models/ErrorDTO';
import { SubjectOutcome } from '../models/SubjectOutcomeDTO';

@Injectable({ providedIn: 'root' })
export class SubjectOutomeService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  // GET /subject/{subjectId}/outcome?activeTerm=true|false
  getOutcomesBySubject(subjectId: number, activeTerm?: boolean): Observable<SubjectOutcome[]> {
    let url = `${this.baseUrl}/subject/${subjectId}/outcome`;
    if (activeTerm !== undefined) {
      url += `?activeTerm=${activeTerm}`;
    }
    return this.http.get<SubjectOutcome[]>(url).pipe(catchError(this.handleError));
  }

  // GET /subject/competency/{competencyId}/outcome
  getOutcomesByCompetency(competencyId: number): Observable<SubjectOutcome[]> {
    return this.http.get<SubjectOutcome[]>(`${this.baseUrl}/subject/competency/${competencyId}/outcome`).pipe(catchError(this.handleError));
  }

  // GET /subject/outcome/{id}
  getOutcomeById(id: number): Observable<SubjectOutcome> {
    return this.http.get<SubjectOutcome>(`${this.baseUrl}/subject/outcome/${id}`).pipe(catchError(this.handleError));
  }

  // POST /subject/{subjectId}/competency/{competencyId}/outcome
  createOutcome(subjectId: number, competencyId: number, data: SubjectOutcome): Observable<SubjectOutcome> {
    return this.http.post<SubjectOutcome>(`${this.baseUrl}/subject/${subjectId}/competency/${competencyId}/outcome`, data).pipe(catchError(this.handleError));
  }

  // PUT /subject/outcome/{id}
  updateOutcome(id: number, data: SubjectOutcome): Observable<SubjectOutcome> {
    return this.http.put<SubjectOutcome>(`${this.baseUrl}/subject/outcome/${id}`, data).pipe(catchError(this.handleError));
  }

  // DELETE /subject/outcome/{id}
  deleteOutcome(id: number): Observable<SubjectOutcome> {
    return this.http.delete<SubjectOutcome>(`${this.baseUrl}/subject/outcome/${id}`).pipe(catchError(this.handleError));
  }

  private handleError(error: any): Observable<never> {
    let err: ErrorResponse = { error: 'Error', message: 'Ocurrió un error inesperado' };
    if (error.error) {
      err = error.error;
    }
    return throwError(() => err);
  }
}
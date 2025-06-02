import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ErrorResponse } from '../models/ErrorDTO';
import { SubjectOutcome } from '../models/SubjectOutcomeDTO';

@Injectable({ providedIn: 'root' })
export class SubjectOutomeService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  // GET /subject/{subjectId}/outcome?activeTerm=true|false
  getOutcomesBySubject(subjectId: number, activateTerm:boolean = true): Observable<SubjectOutcome[]> {
    let url = `${this.baseUrl}/subject/${subjectId}/outcome`;
    let params = new HttpParams();
    params = params.append('activeTerm', activateTerm.toString());
    return this.http.get<SubjectOutcome[]>(url,{params}).pipe(catchError(this.handleError));
  }

  getOutcomesBySubjectAndTerm(subjectId: number, termId: number): Observable<SubjectOutcome[]> {
    let url = `${this.baseUrl}/subject/${subjectId}/term/${termId}/outcome`;
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

    console.log('Error en SubjectOutcomeService:', err);
    return throwError(() => err);
  }
}

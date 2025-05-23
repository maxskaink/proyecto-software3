import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { SubjectCompetency } from '../models/SubjectDTO';
import { ErrorResponse } from '../models/ErrorDTO';

@Injectable({
  providedIn: 'root'
})
export class SubjectCompetencyService {
  private baseUrl = 'http://localhost:8080'; 

  constructor(private http: HttpClient) {}

  getCompetenciesByAsignature(asignatureId: number): Observable<SubjectCompetency[]> {
    return this.http.get<SubjectCompetency[]>(`${this.baseUrl}/subject/${asignatureId}/competency`)
      .pipe(catchError(this.handleError));
  }

  // GET /subject/competency/{id}
  getCompetencyById(id: number): Observable<SubjectCompetency> {
    return this.http.get<SubjectCompetency>(`${this.baseUrl}/subject/competency/${id}`)
      .pipe(catchError(this.handleError));
  }

  // POST /subject/{subjectId}/competency
  assignCompetencyToSubject(subjectId: number, data: Partial<SubjectCompetency>): Observable<SubjectCompetency> {
    return this.http.post<SubjectCompetency>(`${this.baseUrl}/subject/${subjectId}/competency`, data)
      .pipe(catchError(this.handleError));
  }

  // PUT /subject/competency/{id}
  updateCompetency(id: number, data: SubjectCompetency): Observable<SubjectCompetency> {
    return this.http.put<SubjectCompetency>(`${this.baseUrl}/subject/competency/${id}`, data)
      .pipe(catchError(this.handleError));
  }

  // DELETE /subject/competency/{id}
  deleteCompetency(id: number): Observable<SubjectCompetency> {
    return this.http.delete<SubjectCompetency>(`${this.baseUrl}/subject/competency/${id}`)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: any): Observable<never> {
    let err: ErrorResponse = { error: 'Error', message: 'Ocurrió un error inesperado' };
    if (error.error) {
      err = error.error;
    }
    return throwError(() => err);
  }
}

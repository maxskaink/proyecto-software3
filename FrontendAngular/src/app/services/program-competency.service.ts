import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ErrorResponse } from '../models/ErrorDTO';
import { ProgramCompetency } from '../models/ProgramCompetencyDTO';

@Injectable({ providedIn: 'root' })
export class ProgramCompetencyService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  // GET /program/competency
  getAll(): Observable<ProgramCompetency[]> {
    return this.http.get<ProgramCompetency[]>(`${this.baseUrl}/program/competency`).pipe(catchError(this.handleError));
  }

  // GET /program/competency/{id}
  getById(id: number): Observable<ProgramCompetency> {
    return this.http.get<ProgramCompetency>(`${this.baseUrl}/program/competency/${id}`).pipe(catchError(this.handleError));
  }

  // POST /program/competency
  create(data: ProgramCompetency): Observable<ProgramCompetency> {
    return this.http.post<ProgramCompetency>(`${this.baseUrl}/program/competency`, data).pipe(catchError(this.handleError));
  }

  // PUT /program/competency/{id}
  update(id: number, data: ProgramCompetency): Observable<ProgramCompetency> {
    return this.http.put<ProgramCompetency>(`${this.baseUrl}/program/competency/${id}`, data).pipe(catchError(this.handleError));
  }

  // DELETE /program/competency/{id}
  delete(id: number): Observable<ProgramCompetency> {
    return this.http.delete<ProgramCompetency>(`${this.baseUrl}/program/competency/${id}`).pipe(catchError(this.handleError));
  }

  private handleError(error: any): Observable<never> {
    let err: ErrorResponse = { error: 'Error', message: 'Ocurrió un error inesperado' };
    if (error.error) {
      err = error.error;
    }
    return throwError(() => err);
  }
}

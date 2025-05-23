import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ProgramOutcome} from '../models/ProgramDTO';
import { ErrorResponse } from '../models/ErrorDTO';

@Injectable({ providedIn: 'root' })
export class ProgramOutcomeService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  // GET /program/outcome
  getAll(): Observable<ProgramOutcome[]> {
    return this.http.get<ProgramOutcome[]>(`${this.baseUrl}/program/outcome`).pipe(catchError(this.handleError));
  }

  // GET /program/outcome/{id}
  getById(id: number): Observable<ProgramOutcome> {
    return this.http.get<ProgramOutcome>(`${this.baseUrl}/program/outcome/${id}`).pipe(catchError(this.handleError));
  }

  // PUT /program/outcome/{id}
  update(id: number, data: ProgramOutcome): Observable<ProgramOutcome> {
    return this.http.put<ProgramOutcome>(`${this.baseUrl}/program/outcome/${id}`, data).pipe(catchError(this.handleError));
  }

  private handleError(error: any): Observable<never> {
    let err: ErrorResponse = { error: 'Error', message: 'Ocurrió un error inesperado' };
    if (error.error) {
      err = error.error;
    }
    return throwError(() => err);
  }
}

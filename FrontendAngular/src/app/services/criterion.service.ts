import { CriterionDTO } from '../models/CirterionDTO';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ErrorResponse } from '../models/ErrorDTO';

@Injectable({
  providedIn: 'root'
})
export class CriterionService {
  private baseUrl = 'http://localhost:8080'; 

  constructor(private http: HttpClient) { }

  // GET /criterion
  getCriterionById(id: number): Observable<CriterionDTO> {
    return this.http.get<CriterionDTO>(`${this.baseUrl}/criterion/${id}`)
    .pipe(
      catchError(this.handleError)
    );
  }

  // GET /criterion by rubric
  getAllCriterionByRubric(rubricId: number): Observable<CriterionDTO[]> {
    return this.http.get<CriterionDTO[]>(`${this.baseUrl}/criterion/rubric/${rubricId}`)
    .pipe(
      catchError(this.handleError)
    );
  }


  // POST /criterion
    assignCriterionToRubric(rubricId: number, data: Partial<CriterionDTO>): Observable<CriterionDTO> {
        return this.http.post<CriterionDTO>(`${this.baseUrl}/criterion/rubric/${rubricId}`, data)
        .pipe(
        catchError(this.handleError)
        );
    }

    // PUT /criterion
    updateCriterion(id: number, data: CriterionDTO): Observable<CriterionDTO> {
        return this.http.put<CriterionDTO>(`${this.baseUrl}/criterion/${id}`, data)
        .pipe(
        catchError(this.handleError)
        );
    }
    
    // DELETE /criterion
    deleteCriterion(id: number): Observable<CriterionDTO> {
        return this.http.delete<CriterionDTO>(`${this.baseUrl}/criterion/${id}`)
        .pipe(
        catchError(this.handleError)
        );
    }

  private handleError(error: any): Observable<never> {
    let err: ErrorResponse = { error: 'Error', message: 'Ocurrió un error inesperado' };
    if (error.error) {
      err = error.error;
    }
    return throwError(() => err);
  }
}
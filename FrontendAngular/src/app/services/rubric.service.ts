import { RubricDTO } from '../models/RubricDTO';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ErrorResponse } from '../models/ErrorDTO';

@Injectable({
  providedIn: 'root'
})
export class RubricService {
    private baseUrl = 'http://localhost:8080'; 

    constructor(private http: HttpClient) { }

    // GET /rubric
    getRubricById(id: number): Observable<RubricDTO> {
        return this.http.get<RubricDTO>(`${this.baseUrl}/rubric/${id}`)
        .pipe(
            catchError(this.handleError)
        );
    }

    // GET /rubric by subject outcome
    getRubricBySubjectOutcome(subjectOutcomeId: number): Observable<RubricDTO> {
        return this.http.get<RubricDTO>(`${this.baseUrl}/rubric/outcome/${subjectOutcomeId}`)
        .pipe(
            catchError(this.handleError)
        );
    }

    // Get all rubrics in subject
    getAllRubricsBySubject(subjectId: number): Observable<RubricDTO[]> {
        return this.http.get<RubricDTO[]>(`${this.baseUrl}/rubric/subject/${subjectId}`)
        .pipe(
            catchError(this.handleError)
        );
    }

    // POST /rubric
    assignRubricToSubjectOutcome(subjectOutcomeId: number, data: Partial<RubricDTO>): Observable<RubricDTO> {
        return this.http.post<RubricDTO>(`${this.baseUrl}/rubric/outcome/${subjectOutcomeId}`, data)
        .pipe(
            catchError(this.handleError)
        );
    }

    // PUT /rubric
    updateRubric(id: number, data: Partial<RubricDTO>): Observable<RubricDTO> {
        return this.http.put<RubricDTO>(`${this.baseUrl}/rubric/${id}`, data)
        .pipe(
            catchError(this.handleError)
        );
    }

    // DELETE /rubric
    deleteRubric(id: number): Observable<RubricDTO> {
        return this.http.delete<RubricDTO>(`${this.baseUrl}/rubric/${id}`)
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
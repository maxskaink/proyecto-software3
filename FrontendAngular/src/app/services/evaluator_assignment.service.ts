import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ErrorResponse } from '../models/ErrorDTO';
import { EvaluatorAssignment } from '../models/EvaluatorAssignmentDTO';

@Injectable({
  providedIn: 'root'
})
export class EvaluatorAssignmentService {
    private baseUrl = 'http://localhost:8080';
    
    constructor(private http: HttpClient) {}
    
    // POST /evaluatorAssignment/evaluator/{evaluatorUid}/subject/{subjectId}
    assignevaluatorToSubjectOutcomeInActiveTerm(evaluatorUid: string, subjectId: number): Observable<EvaluatorAssignment> {
        return this.http.post<EvaluatorAssignment>(`${this.baseUrl}/evaluatorAssignment/evaluator/${evaluatorUid}/subject/${subjectId}`,null).
        pipe(catchError(this.handleError));
    }

    // GET /evaluatorAssignment/{id}
    getAssignmentById(id: number): Observable<EvaluatorAssignment> {
        return this.http.get<EvaluatorAssignment>(`${this.baseUrl}/evaluatorAssignment/${id}`).
        pipe(catchError(this.handleError));
    }

    // GET /evaluatorAssignment/evaluator/{evaluatorUid}/subject/{subjectId}
    getAssignmentByevaluatorAndSubjectOutcomeInActiveTerm(evaluatorUid: string, subjectId: number): Observable<EvaluatorAssignment> {
        return this.http.get<EvaluatorAssignment>(`${this.baseUrl}/evaluatorAssignment/evaluator/${evaluatorUid}/subject/${subjectId}`).
        pipe(catchError(this.handleError));
    }

    // GET /evaluatorAssignment
    getAllAssignments(): Observable<EvaluatorAssignment[]> {
        return this.http.get<EvaluatorAssignment[]>(`${this.baseUrl}/evaluatorAssignment`).
        pipe(catchError(this.handleError));
    }

    //GET /evaluatorAssignment/subject/{subjectId}
    getAssignmentsBySubjectOutcome(subjectId: number): Observable<EvaluatorAssignment[]> {
        return this.http.get<EvaluatorAssignment[]>(`${this.baseUrl}/evaluatorAssignment/subject/${subjectId}`).
        pipe(catchError(this.handleError));
    }

    // GET /evaluatorAssignment/evaluator/{evaluatorUid}
    getAssignmentsByevaluator(evaluatorUid: string): Observable<EvaluatorAssignment[]> {
        return this.http.get<EvaluatorAssignment[]>(`${this.baseUrl}/evaluatorAssignment/evaluator/${evaluatorUid}`).
        pipe(catchError(this.handleError));
    }

    // DELETE /evaluatorAssignment/{id}
    deleteAssignment(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/evaluatorAssignment/${id}`).
        pipe(catchError(this.handleError));
    }

    // DELETE /evaluatorAssignment/evaluator/{evaluatorUid}/subject/{subjectId}
    deleteAssignmentByevaluatorAndSubjectOutcome(evaluatorUid: string, subjectId: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/evaluatorAssignment/evaluator/${evaluatorUid}/subject/${subjectId}`).
        pipe(catchError(this.handleError));
    }

    private handleError(error: any): Observable<never> {
        let err: ErrorResponse = { error: 'Error', message: 'Ocurrió un error inesperado' };
        if (error.error) {
        err = error.error;
        }
        return throwError(() => err);
    }
}
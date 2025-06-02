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
    private baseUrl = 'http://localhost:8081';

    constructor(private http: HttpClient) {}

    // POST /evaluator-assignment/evaluator/{evaluatorUid}/outcome/{subjectId}
    assignevaluatorToSubjectOutcomeInActiveTerm(evaluatorUid: string, subjectOutcomeId: number): Observable<EvaluatorAssignment> {
        return this.http.post<EvaluatorAssignment>(`${this.baseUrl}/evaluator-assignment/evaluator/${evaluatorUid}/outcome/${subjectOutcomeId}`,null).
        pipe(catchError(this.handleError));
    }

    // GET /evaluator-assignment/{id}
    getAssignmentById(id: number): Observable<EvaluatorAssignment> {
        return this.http.get<EvaluatorAssignment>(`${this.baseUrl}/evaluator-assignment/${id}`).
        pipe(catchError(this.handleError));
    }

    // GET /evaluator-assignment/evaluator/{evaluatorUid}/subject/{subjectId}
    getAssignmentByevaluatorAndSubjectOutcomeInActiveTerm(evaluatorUid: string, subjectId: number): Observable<EvaluatorAssignment> {
        return this.http.get<EvaluatorAssignment>(`${this.baseUrl}/evaluator-assignment/evaluator/${evaluatorUid}/subject/${subjectId}`).
        pipe(catchError(this.handleError));
    }

    // GET /evaluator-assignment
    getAllAssignments(): Observable<EvaluatorAssignment[]> {
        return this.http.get<EvaluatorAssignment[]>(`${this.baseUrl}/evaluator-assignment`).
        pipe(catchError(this.handleError));
    }

    //GET /evaluator-assignment/outcome/{outcomeId}
    getAssignmentsBySubjectOutcome(subjectOutcomeId: number): Observable<EvaluatorAssignment[]> {
        return this.http.get<EvaluatorAssignment[]>(`${this.baseUrl}/evaluator-assignment/outcome/${subjectOutcomeId}`).
        pipe(catchError(this.handleError));
    }

    // GET /evaluator-assignment/evaluator/{evaluatorUid}
    getAssignmentsByevaluator(evaluatorUid: string): Observable<EvaluatorAssignment[]> {
        return this.http.get<EvaluatorAssignment[]>(`${this.baseUrl}/evaluator-assignment/evaluator/${evaluatorUid}`).
        pipe(catchError(this.handleError));
    }

    // DELETE /evaluator-assignment/{id}
    deleteAssignment(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/evaluator-assignment/${id}`).
        pipe(catchError(this.handleError));
    }

    // DELETE /evaluator-assignment/evaluator/{evaluatorUid}/subject/{subjectId}
    deleteAssignmentByevaluatorAndSubjectOutcome(evaluatorUid: string, subjectId: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/evaluator-assignment/evaluator/${evaluatorUid}/subject/${subjectId}`).
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

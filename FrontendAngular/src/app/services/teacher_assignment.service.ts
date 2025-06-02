import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ErrorResponse } from '../models/ErrorDTO';
import { TeacherAssignment } from '../models/TeacherAssignmentDTO';

@Injectable({
  providedIn: 'root'
})
export class TeacherAssignmentService {
    private baseUrl = 'http://localhost:8081';

    constructor(private http: HttpClient) {}

    // POST /teacher-assignment/teacher/{teacherUid}/subject/{subjectId}
    assignTeacherToSubjectInActiveTerm(teacherUid: string, subjectId: number): Observable<TeacherAssignment> {
        return this.http.post<TeacherAssignment>(`${this.baseUrl}/teacher-assignment/teacher/${teacherUid}/subject/${subjectId}`,null).
        pipe(catchError(this.handleError));
    }

    // GET /teacher-assignment/{id}
    getAssignmentById(id: number): Observable<TeacherAssignment> {
        return this.http.get<TeacherAssignment>(`${this.baseUrl}/teacher-assignment/${id}`).
        pipe(catchError(this.handleError));
    }

    // GET /teacher-assignment/teacher/{teacherUid}/subject/{subjectId}
    getAssignmentByTeacherAndSubjectInActiveTerm(teacherUid: string, subjectId: number): Observable<TeacherAssignment> {
        return this.http.get<TeacherAssignment>(`${this.baseUrl}/teacher-assignment/teacher/${teacherUid}/subject/${subjectId}`).
        pipe(catchError(this.handleError));
    }

    // GET /teacher-assignment
    getAllAssignments(): Observable<TeacherAssignment[]> {
        return this.http.get<TeacherAssignment[]>(`${this.baseUrl}/teacher-assignment`).
        pipe(catchError(this.handleError));
    }

    //GET /teacher-assignment/subject/{subjectId}
    getAssignmentsBySubject(subjectId: number): Observable<TeacherAssignment[]> {
        return this.http.get<TeacherAssignment[]>(`${this.baseUrl}/teacher-assignment/subject/${subjectId}`).
        pipe(catchError(this.handleError));
    }

    // GET /teacher-assignment/teacher/{teacherUid}
    getAssignmentsByTeacher(teacherUid: string): Observable<TeacherAssignment[]> {
        return this.http.get<TeacherAssignment[]>(`${this.baseUrl}/teacher-assignment/teacher/${teacherUid}`).
        pipe(catchError(this.handleError));
    }

    // DELETE /teacher-assignment/{id}
    deleteAssignment(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/teacher-assignment/${id}`).
        pipe(catchError(this.handleError));
    }

    // DELETE /teacher-assignment/teacher/{teacherUid}/subject/{subjectId}
    deleteAssignmentByTeacherAndSubject(teacherUid: string, subjectId: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/teacher-assignment/teacher/${teacherUid}/subject/${subjectId}`).
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

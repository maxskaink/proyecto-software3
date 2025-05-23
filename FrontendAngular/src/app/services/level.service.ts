import { LevelDTO } from '../models/LevelDTO';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ErrorResponse } from '../models/ErrorDTO';

@Injectable({
  providedIn: 'root'
})
export class LevelService {
    private baseUrl = 'http://localhost:8080'; 
    
    constructor(private http: HttpClient) { }
    
    // GET /level
    getLevelById(id: number): Observable<LevelDTO> {
        return this.http.get<LevelDTO>(`${this.baseUrl}/level/${id}`)
        .pipe(
        catchError(this.handleError)
        );
    }
    
    // GET /level by criterion
    getAllLevelsByCriterion(criterionId: number): Observable<LevelDTO[]> {
        return this.http.get<LevelDTO[]>(`${this.baseUrl}/level/criterion/${criterionId}`)
        .pipe(
        catchError(this.handleError)
        );
    }

    // POST /level
    assignLevelToCriterion(criterionId: number, data: Partial<LevelDTO>): Observable<LevelDTO> {
        return this.http.post<LevelDTO>(`${this.baseUrl}/level/criterion/${criterionId}`, data)
        .pipe(
            catchError(this.handleError)
        );
    }

    // PUT /level
    updateLevel(id: number, data: LevelDTO): Observable<LevelDTO> {
        return this.http.put<LevelDTO>(`${this.baseUrl}/level/${id}`, data)
        .pipe(
            catchError(this.handleError)
        );
    }

    // DELETE /level
    deleteLevel(id: number): Observable<LevelDTO> {
        return this.http.delete<LevelDTO>(`${this.baseUrl}/level/${id}`)
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
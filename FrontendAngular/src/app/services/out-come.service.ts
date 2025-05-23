import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OutCome } from '../models/SubjectOutcomeDTO';

@Injectable({
  providedIn: 'root'
})
export class OutComeService {
  private baseUrl = 'http://localhost:8080"/subject'; // Cambia según tu backend

  constructor(private http: HttpClient) {}

  listAll(subjectId: number): Observable<OutCome[]> {
    return this.http.get<OutCome[]>(`${this.baseUrl}/${subjectId}/outcome`);
  }
}
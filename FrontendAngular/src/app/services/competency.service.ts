import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { competencyDTO } from '../models/CompetencyDTO';

@Injectable({
  providedIn: 'root'
})
export class CompetencyService {
  private baseUrl = 'http://localhost:8080"/subject'; // Cambia según tu backend

  constructor(private http: HttpClient) {}

  getCompetenciesByAsignature(asignatureId: number): Observable<competencyDTO[]> {
    return this.http.get<competencyDTO[]>(`${this.baseUrl}/${asignatureId}/competency`);
  }
}

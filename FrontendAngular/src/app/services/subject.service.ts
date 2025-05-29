import { Injectable } from '@angular/core';
import { SubjectDTO } from '../models/SubjectDTO';
import { Observable } from 'rxjs';
import { of } from 'rxjs';
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./auth.service";


@Injectable({
  providedIn: 'root'
})
export class SubjectService {
  private path: string = 'http://localhost:8081/subject';
  constructor(private http:HttpClient) {}

  getAssignedAsignatures(): Observable<SubjectDTO[]> {
    return this.http.get<SubjectDTO[]>(`${this.path}/assigned`);
  }
  getAsignatureID(id:number):  Observable<SubjectDTO>{
    return of({id: 1,name: 'Ingenieria Software', description: ' profundiza en los aspectos avanzados del desarrollo de software, construyendo sobre los fundamentos establecidos en cursos anteriores. Generalmente, esta materia se enfoca en el diseño, la arquitectura y la calidad del software en proyectos de mayor escala y complejidad.' })
  }
}

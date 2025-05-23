import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AsignatureDTO } from '../models/SubjectDTO';
import { Observable } from 'rxjs';
import { of } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AsignatureService {

  //private urlEndpoint: string = 'http://localhost:5000/api/reserva';

  constructor() {}

  getAsignature(): Observable<AsignatureDTO[]> {
    return of([
      { id: 1,title: 'Ingenieria Software', description: 'Materia orientada al desrrollo de sotware web' },
      { id: 2, title: 'Lab. Ingenieria Software II', description: 'Fundamentos de física moderna' },
      { id: 3,title: 'POO', description: 'Conceptos básicos de química' },
      { id: 4, title: 'Base de dato', description: 'Asignatura de cálculo' },
      { id: 5,title: 'Base de dato', description: 'Asignatura de cálculo' },
      
    ]);
  }
  getAsignatureID(id:number):  Observable<AsignatureDTO>{
    return of({id: 1,title: 'Ingenieria Software', description: ' profundiza en los aspectos avanzados del desarrollo de software, construyendo sobre los fundamentos establecidos en cursos anteriores. Generalmente, esta materia se enfoca en el diseño, la arquitectura y la calidad del software en proyectos de mayor escala y complejidad.' })
  }
}

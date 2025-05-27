import { Injectable } from '@angular/core';
import { SubjectDTO } from '../models/SubjectDTO';
import { Observable } from 'rxjs';
import { of } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AsignatureService {

  //private urlEndpoint: string = 'http://localhost:5000/api/reserva';

  constructor() {}

  getAsignature(): Observable<SubjectDTO[]> {
    return of([
      { id: 1,title: 'Programación I', description: 'Fundamentos de programación en Java' },
      { id: 2, title: 'Estructuras de Datos', description: 'Uso de estructuras básicas como listas, pilas, colas y árboles' },
      { id: 3,title: 'POO', description: 'Conceptos básicos de química' },  
    { id: 4, title: 'Base de dato', description: 'Asignatura de cálculo' },
//       id: 5,title: 'Base de dato', description: 'Asignatura de cálculo' },
//  Comentado para que tenga sentido con el back dado que se necesitan hacer prubeas de integracion       
    ]);
  }
  getAsignatureID(id:number):  Observable<SubjectDTO>{
    return of({id: 1,title: 'Ingenieria Software', description: ' profundiza en los aspectos avanzados del desarrollo de software, construyendo sobre los fundamentos establecidos en cursos anteriores. Generalmente, esta materia se enfoca en el diseño, la arquitectura y la calidad del software en proyectos de mayor escala y complejidad.' })
  }
}

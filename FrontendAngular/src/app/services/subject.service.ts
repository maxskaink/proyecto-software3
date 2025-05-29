import { Injectable, numberAttribute } from '@angular/core';
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

  getAssignedSubject(): Observable<SubjectDTO[]> {
    return this.http.get<SubjectDTO[]>(`${this.path}/assigned`);
  }
  getSubjectID(id:number):  Observable<SubjectDTO>{
    return this.http.get<SubjectDTO>(`${this.path}/${id}`);
  }
  getAllSubjects(): Observable<SubjectDTO[]>{
    return this.http.get<SubjectDTO[]>(`${this.path}`);
  }
  putSubject(id:number,newSubject:SubjectDTO){
    return this.http.put<SubjectDTO>(`${this.path}/${id}`, newSubject);
  }
  deleteSubject(id:number){
    return this.http.delete<SubjectDTO>(`${this.path}/${id}`);
  }
}

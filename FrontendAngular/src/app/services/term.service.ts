import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {TermDTO} from "../models/TermDTO";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TermService {
  private url: string = "http://localhost:8080/term";

  constructor(private http:HttpClient) { }

  addTerm(term:TermDTO): Observable<TermDTO>{
    return this.http.post<TermDTO>(`${this.url}`, term);
  }

  getTerms(): Observable<TermDTO[]> {
    return this.http.get<TermDTO[]>(`${this.url}`);
  }

  getActiveTerm() :Observable<TermDTO>{
    return this.http.get<TermDTO>(`${this.url}/active`);
  }

  putActiveTerm(idTerm:number):Observable<TermDTO>{
    return this.http.put<TermDTO>(`${this.url}/${idTerm}/activate`,{});
  }
}

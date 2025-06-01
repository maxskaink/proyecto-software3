import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EditStateService {
  private editState = new BehaviorSubject<boolean>(false);
  editState$ = this.editState.asObservable();

  setEditState(state: boolean) {
    this.editState.next(state);
  }

  getEditState() {
    return this.editState.getValue();
  }
  constructor() { }
}

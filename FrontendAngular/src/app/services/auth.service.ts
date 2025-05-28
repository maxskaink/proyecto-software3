import { Injectable } from '@angular/core';
import { Auth, signInWithEmailAndPassword, signOut, User } from '@angular/fire/auth';
import { onAuthStateChanged } from 'firebase/auth';
import { Observable, BehaviorSubject } from 'rxjs';
import { Firestore, doc, getDoc } from '@angular/fire/firestore';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private role$ = new BehaviorSubject<string | null>(null);
  private name$ = new BehaviorSubject<string | null>(null);
  private currentUserSubject = new BehaviorSubject<User | null>(null);

  constructor(private afAuth: Auth, private firestore: Firestore) {
    // Escucha cambios de usuario y actualiza datos adicionales y usuario actual
    onAuthStateChanged(this.afAuth, async (user) => {
      this.currentUserSubject.next(user);
      if (user) {
        const docSnap = await getDoc(doc(this.firestore, 'SERA', user.uid));
        if (docSnap.exists()) {
          const data = docSnap.data();
          this.role$.next(data['role'] ?? null);
          this.name$.next(data['name'] ?? null);
        } else {
          this.role$.next(null);
          this.name$.next(null);
        }
      } else {
        this.role$.next(null);
        this.name$.next(null);
      }
    });
  }

  async login(email: string, password: string) {
    await signInWithEmailAndPassword(this.afAuth, email, password);
    // El listener onAuthStateChanged se encargará de actualizar role, name y usuario actual
  }

  get isLoggedIn$(): Observable<boolean> {
    return this.currentUserSubject.asObservable().pipe(
      // Puedes usar map si importas 'map' de rxjs
      // map(user => !!user)
      // O simplemente:
      (source) => new Observable(subscriber => {
        source.subscribe(user => subscriber.next(!!user));
      })
    );
  }

  get currentUser(): Observable<User | null> {
    return this.currentUserSubject.asObservable();
  }

  // Método síncrono para obtener el usuario actual (si lo necesitas)
  getCurrentUser(): User | null {
    return this.afAuth.currentUser;
  }

  get role(): Observable<string | null> {
    return this.role$.asObservable();
  }

  get name(): Observable<string | null> {
    return this.name$.asObservable();
  }

  logout() {
    return signOut(this.afAuth);
  }

  async getToken(): Promise<string | null> {
    const user = this.afAuth.currentUser;
    if (user) {
      return await user.getIdToken();
    }
    return null;
  }

  // Placeholder para getAllUsers, implementa según tu lógica
  getAllUsers(): Observable<any[]> {
    // Implementa aquí tu lógica para obtener todos los usuarios
    return new Observable<any[]>(); // Cambia esto por tu implementación real
  }
}
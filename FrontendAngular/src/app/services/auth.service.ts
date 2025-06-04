import { Injectable } from '@angular/core';
import { Auth, signInWithEmailAndPassword, signOut, User } from '@angular/fire/auth';
import { onAuthStateChanged } from 'firebase/auth';
import { Observable, BehaviorSubject, firstValueFrom } from 'rxjs';
import { filter } from 'rxjs/operators';
import {Firestore, doc, getDoc, collection, getDocs} from '@angular/fire/firestore';
import { TeacherDTO } from '../models/TeacherDTO';
import {setUserId} from "@angular/fire/analytics";

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

  /**
   * Check if the user is logged in
   */
  get isLoggedIn$(): Observable<boolean> {
    return this.currentUserSubject.asObservable().pipe(
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
  /**
   * Get the current user's ID token
   */
  async getToken(): Promise<string | null> {
    const user = await firstValueFrom(
      this.currentUser.pipe(
        filter((u): u is User => u !== null)
      )
    );
    return user.getIdToken();
  }
  /**
   * Receive the user for map to TeacherDTO
   *
   */
  private mapToTeacherDTO(userData: any): TeacherDTO {
    return {
      academicTitle: userData.academicTitle || '',
      identification: userData.identification || 0,
      identificationType: userData.identificationType || '',
      lastName: userData.lastName || '',
      name: userData.name || '',
      role: userData.role || '',
      typeTeacher: userData.typeTeacher || ''
    };
  }


  /**
   * Get all User of firebase and  map the user whit mapToTEacherDTO()
   */
  getAllUsers(): Observable<TeacherDTO[]> {
    return new Observable<TeacherDTO[]>((subscriber) => {
      (async () => {
        try {
          // Cambia esto: accede a la colección en lugar del documento
          const usersCollection = collection(this.firestore, 'SERA');
          const usersSnapshot = await getDocs(usersCollection);

          if (!usersSnapshot.empty) {
            const teachersDTO = usersSnapshot.docs.map(doc => {
              let response = this.mapToTeacherDTO(doc.data());
              response.id = doc.id;
              return response;
            });

            subscriber.next(teachersDTO);
          } else {
            subscriber.next([]);
          }
          subscriber.complete();
        } catch (error) {
          subscriber.error(error);
        }
      })();
    });
  }

  /**
   * Get a user by ID and map the user to TeacherDTO
   * @param userId - The ID of the user to retrieve
   * @returns An observable that emits the TeacherDTO of the user
   */
  getUserById(userId: string): Observable<TeacherDTO> {
    return new Observable<TeacherDTO>((subscriber) => {
      (async () => {
        try {
          const userDoc = await getDoc(doc(this.firestore, 'SERA', userId));
          if (userDoc.exists()) {
            const userData = userDoc.data();
            subscriber.next(this.mapToTeacherDTO(userData));
          } else {
            subscriber.error(new Error('User not found'));
          }
          subscriber.complete();
        } catch (error) {
          subscriber.error(error);
        }
      })();
    });
  }

  //TODO implement these methods
  createUser(teacher: TeacherDTO): Observable<void> {
    return new Observable<void>((subscriber) => subscriber.complete);
  }

  updateUser(teacher: TeacherDTO): Observable<void> {
    return new Observable<void>((subscriber) => subscriber.complete);
  }

}

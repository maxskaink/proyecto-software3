import { Injectable } from '@angular/core';
import { Auth, signInWithEmailAndPassword, signOut, User } from '@angular/fire/auth';
import { onAuthStateChanged } from 'firebase/auth';
import { Observable, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { Firestore, collection, doc, getDoc, getDocs } from '@angular/fire/firestore';
@Injectable({ providedIn: 'root' })
export class AuthService {
  private userData: any = null;

  constructor(private afAuth: Auth, private firestore: Firestore) {}

  async login(email: string, password: string) {
    const userCredential = await signInWithEmailAndPassword(this.afAuth, email, password);
    const user = userCredential.user;

    const docSnap = await getDoc(doc(this.firestore, 'SERA', user.uid));
    if (docSnap.exists()) {
      const data = docSnap.data();
      const role = data['role'];
      const name= data['name'];
    
      localStorage.setItem('userRole', role);
      localStorage.setItem('name',name);
      
      this.userData = {
        uid: user.uid,
        email: user.email,
        role: role,
        name: name
      };
      console.log("ingreso exitosos")
      console.log(this.userData)
    } else {
      console.error('Documento de usuario no encontrado en Firestore');
      // Puedes lanzar un error o asignar valores por defecto
      this.userData = {
        uid: user.uid,
        email: user.email,
        role: null,
        name: null
      };
    }
  }
  async getAllUsers(): Promise<any[]> {
    const usersCollection = collection(this.firestore, 'SERA');
    const querySnapshot = await getDocs(usersCollection);
  
    const users: any[] = [];
    querySnapshot.forEach((doc) => {
      users.push({
        id: doc.id,
        ...doc.data()
      });
    });
  
    return users;
  }

  get isLoggedIn(): boolean {
    return !!this.userData;
  }

   get currentUser() {
    return this.userData;
  }

  logout() {
    this.userData = null;
    return signOut(this.afAuth);
  }
}
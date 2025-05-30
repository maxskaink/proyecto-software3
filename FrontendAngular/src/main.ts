import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideHttpClient, withInterceptors,withFetch } from '@angular/common/http';
import { authInterceptor } from './app/interceptors/auth.interceptor';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { routes } from './app/app.routes';
import { appConfig } from './app/app.config';

import { InjectionToken } from '@angular/core';
import { environment } from './environments/environment';
import { getFirestore } from 'firebase/firestore';
import { provideFirestore } from '@angular/fire/firestore';
import { provideFirebaseApp } from '@angular/fire/app';
import { getAuth, provideAuth } from '@angular/fire/auth';
import { initializeApp } from 'firebase/app';

export const APP_CONFIG = new InjectionToken('app.config');

export const appConfigProvider = {
  provide: APP_CONFIG,
  useValue: appConfig
};

bootstrapApplication(AppComponent, {
  providers: [
    provideFirebaseApp(() => initializeApp(environment.firebaseConfig)),
    provideAuth(() => getAuth()),
    provideFirestore(() => getFirestore()),
    provideHttpClient(withInterceptors([authInterceptor]),withFetch()),
    provideRouter(routes, withComponentInputBinding()),
    appConfigProvider
  ]
}).catch((err) => console.error(err));

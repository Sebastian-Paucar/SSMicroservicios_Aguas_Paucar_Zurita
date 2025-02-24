import { Routes } from '@angular/router';
import {LoginComponent} from './Pages/main/login/login.component';
import {authGuard} from './services/auth.guard';

export const routes: Routes = [

  {
    path: 'login',
    component: LoginComponent
  }
];

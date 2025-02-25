import { Routes } from '@angular/router';
import {LoginComponent} from './Pages/main/login/login.component';
import {DashboardComponent} from './Pages/main/dashboard/dashboard/dashboard.component';

export const routes: Routes = [

  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'dashboard',
    component: DashboardComponent
  },
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
];

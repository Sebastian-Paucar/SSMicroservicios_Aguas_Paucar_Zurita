import {Routes} from '@angular/router';
import {LoginComponent} from './Pages/main/login/login.component';
import {DashboardComponent} from './Pages/main/dashboard/dashboard/dashboard.component';
import {LogoutComponent} from './Pages/main/logout/logout.component';
import {AuthGuard} from './services/auth.guard';
import {RegisterComponent} from './Pages/main/register/register/register.component';
import {ProductFormComponent} from './Pages/main/productos/crearProducto/product-form/product-form.component';
import {CarritoComponent} from './Pages/main/carrito/carrito/carrito.component';

export const routes: Routes = [

  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'dashboard',
    component: DashboardComponent
  },
  {path: 'register',
    component:RegisterComponent
  },
  {
    path:"crearProducto",
    component:ProductFormComponent
  },
  {
    path: 'carrito',
    component: CarritoComponent
  },
  {
    path: 'logout',
    component: LogoutComponent,
    canActivate:[AuthGuard]
  },
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
];

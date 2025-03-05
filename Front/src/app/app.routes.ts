import { Routes } from '@angular/router';
import { LoginComponent } from './Pages/main/login/login.component';
import { DashboardComponent } from './Pages/main/dashboard/dashboard/dashboard.component';
import { LogoutComponent } from './Pages/main/logout/logout.component';
import { AuthGuard } from './services/auth.guard';
import { RegisterComponent } from './Pages/main/register/register/register.component';
import { ProductFormComponent } from './Pages/main/productos/crearProducto/product-form/product-form.component';
import { CarritoComponent } from './Pages/main/carrito/carrito/carrito.component';
import { AdminDashboardComponent } from './Pages/AdminDashboard/admin-dashboard/admin-dashboard.component';
import {CategoriaComponent} from './Pages/main/productos/categoria/categoria.component';



export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'carrito', component: CarritoComponent },
  { path: 'logout', component: LogoutComponent, canActivate: [AuthGuard] },
  {
    path: 'admin-dashboard',
    component: AdminDashboardComponent,
     canActivate: [AuthGuard],
     data: { roles: ['ROLE_ADMIN'] },
    children: [
      { path: '', data: { title: 'Dashboard' } },
      { path: 'crearProducto', component: ProductFormComponent, data: { title: 'Crear Producto' } },
      { path: 'crearCategoria', component: CategoriaComponent, data: { title: 'Crear Categoría' } }
    ]
  },
  // {/>
  //   path: 'user-dashboard',
  //   component: UserDashboardComponent,
  //   canActivate: [AuthGuard],
  //   data: { roles: ['ROLE_USER'] },
  //   children: [
  //     { path: '', component: DashboardComponent, data: { title: 'Dashboard' } },
  //     { path: 'carrito', component: CarritoComponent, data: { title: 'Carrito' } }
  //   ]
  // },
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
];

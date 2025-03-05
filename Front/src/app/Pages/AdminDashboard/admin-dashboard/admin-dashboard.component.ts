import { Component } from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {SidebarAdminComponent} from '../sidebar-admin/sidebar-admin.component';
import {NavbarComponent} from '../../navbar/navbar/navbar.component';

@Component({
  selector: 'app-admin-dashboard',
  imports: [

    SidebarAdminComponent,
    RouterOutlet,
    NavbarComponent
  ],
  templateUrl: './admin-dashboard.component.html',
  styles: ``
})
export class AdminDashboardComponent {

}

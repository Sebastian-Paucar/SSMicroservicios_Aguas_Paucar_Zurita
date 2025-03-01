import {Component} from '@angular/core';
import {ProductService} from '../../../../services/product.service';
import {NavbarComponent} from '../../../navbar/navbar/navbar.component';
import {SidebarComponent} from '../../../sidebar/sidebar/sidebar.component';

@Component({
  selector: 'app-dashboard',
  imports: [
    NavbarComponent,
    SidebarComponent
  ],
  templateUrl: './dashboard.component.html',
  styles: ``
})
export class DashboardComponent {
  products: any[];
  constructor(private productService: ProductService) {
    this.products = this.productService.getProducts();
  }
}

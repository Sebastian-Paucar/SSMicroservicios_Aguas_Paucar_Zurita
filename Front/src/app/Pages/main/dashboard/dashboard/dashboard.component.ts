import { Component } from '@angular/core';
import {Input} from 'postcss';
import {ProductService} from '../../../../services/product.service';
import {NavbarComponent} from '../../../navbar/navbar/navbar.component';

@Component({
  selector: 'app-dashboard',
  imports: [
    NavbarComponent
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

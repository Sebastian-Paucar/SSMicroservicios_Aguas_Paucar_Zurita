import { Component, OnInit } from '@angular/core';
import { ProductService} from '../../../../services/product.service';
import { NavbarComponent } from '../../../navbar/navbar/navbar.component';
import { SidebarComponent } from '../../../sidebar/sidebar/sidebar.component';
import {Producto} from '../../../../interfaces/global.interfaces';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [NavbarComponent, SidebarComponent],
  templateUrl: './dashboard.component.html',
  styles: ``
})
export class DashboardComponent implements OnInit {
  products: Producto[] = [];

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.cargarProductos();
  }

  /**
   * Carga la lista de productos desde el backend.
   */
  cargarProductos(): void {
    this.productService.getProductos().subscribe({
      next: (data) => {
        this.products = data;
      },
      error: (err) => {
        console.error('Error al obtener productos:', err);
      }
    });
  }

  /**
   * Obtiene la URL de la imagen almacenada en el servidor.
   */
  obtenerImagenUrl(imagenUrl?: string): string {
    return imagenUrl ? this.productService.obtenerImagenUrl(imagenUrl) : 'assets/no-image.png';
  }
}

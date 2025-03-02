import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../../services/product.service';
import { NavbarComponent } from '../../../navbar/navbar/navbar.component';
import { SidebarComponent } from '../../../sidebar/sidebar/sidebar.component';
import { Producto } from '../../../../interfaces/global.interfaces';
import {NgIf, NgOptimizedImage} from '@angular/common';
import {Subscription} from 'rxjs';
import {SearchService} from '../../../../services/search.service';
import {CartService} from '../../../../services/cart.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [NavbarComponent, SidebarComponent, NgOptimizedImage, NgIf],
  templateUrl: './dashboard.component.html',
  styles: ``
})
export class DashboardComponent implements OnInit {
  products: Producto[] = [];
  filteredProducts: Producto[] = [];
  private searchSubscription!: Subscription;
  constructor(private productService: ProductService,private searchService: SearchService, private cartService: CartService) {}

  ngOnInit(): void {
    this.cargarProductos();

    // Suscribirse al término de búsqueda
    this.searchSubscription = this.searchService.searchQuery$.subscribe(query => {
      this.filtrarProductos(query);
    });
  }

  /**
   * Carga la lista de productos desde el backend.
   */

  cargarProductos(): void {
    this.productService.getProductos().subscribe({
      next: (data) => {
        this.products = data;
        this.filteredProducts = data; // Inicialmente, mostramos todos los productos
      },
      error: (err) => {
        console.error('Error al obtener productos:', err);
      }
    });
  }

  filtrarProductos(query: string) {
    if (!query) {
      this.filteredProducts = this.products;
    } else {
      this.filteredProducts = this.products.filter(p =>
        p.nombre.toLowerCase().includes(query.toLowerCase())
      );
    }
  }
  /**
   * Obtiene la URL de la imagen almacenada en el servidor.
   */
  obtenerImagenUrl(imagenUrl?: string): string {
    return imagenUrl ? this.productService.obtenerImagenUrl(imagenUrl) : 'assets/no-image.png';
  }

  /**
   * Método para comprar un producto.
   * @param producto Producto que se desea comprar.
   */
  comprarProducto(producto: Producto): void {
    this.cartService.addToCart(producto);
  }

  /**
   * Método para mostrar una notificación de éxito o error.
   */
  mostrarNotificacion(mensaje: string, tipo: 'success' | 'error'): void {
    const notificacion = document.createElement('div');
    notificacion.textContent = mensaje;
    notificacion.classList.add(
      'fixed', 'top-5', 'right-5', 'px-4', 'py-2', 'rounded-lg', 'shadow-lg', 'text-white',
      tipo === 'success' ? 'bg-green-500' : 'bg-red-500'
    );

    document.body.appendChild(notificacion);

    setTimeout(() => {
      notificacion.remove();
    }, 3000);
  }
  ngOnDestroy() {
    this.searchSubscription.unsubscribe();
  }
}

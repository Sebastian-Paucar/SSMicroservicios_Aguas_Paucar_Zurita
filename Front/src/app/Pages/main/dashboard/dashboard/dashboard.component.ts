import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../../services/product.service';
import { CategoriaService } from '../../../../services/categoria.service';
import {Categoria, Producto} from '../../../../interfaces/global.interfaces';
import { Subscription } from 'rxjs';
import { SearchService } from '../../../../services/search.service';
import { CartService } from '../../../../services/cart.service';
import {NgIf, NgOptimizedImage} from '@angular/common';
import {SidebarComponent} from '../../../sidebar/sidebar/sidebar.component';
import {NavbarComponent} from '../../../navbar/navbar/navbar.component';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  standalone: true,
  imports: [
    NgOptimizedImage,
    SidebarComponent,
    NavbarComponent,
    NgIf
  ],
  styles: ``
})
export class DashboardComponent implements OnInit {
  products: Producto[] = [];
  filteredProducts: Producto[] = [];
  categories: Categoria[] = [];
  selectedCategories: number[] = [];
  private searchSubscription!: Subscription;

  constructor(
    private productService: ProductService,
    private searchService: SearchService,
    private cartService: CartService,
    private categoriaService: CategoriaService
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
    this.cargarCategorias();
    this.searchSubscription = this.searchService.searchQuery$.subscribe(query => {
      this.filtrarProductos(query);
    });
  }

  cargarProductos(): void {
    this.productService.getProductos().subscribe({
      next: (data) => {
        this.products = data;
        this.filteredProducts = this.products; // Inicializar el filtrado
      },
      error: (err) => {
        console.error('Error al obtener productos:', err);
      }
    });
  }

  cargarCategorias(): void {
    this.categoriaService.getCategorias().subscribe({
      next: (data) => {
        this.categories = data;
      },
      error: (err) => {
        console.error('Error al obtener categorías:', err);
      }
    });
  }

  filtrarProductos(query: string) {
    let productosFiltrados = this.products;

    if (this.selectedCategories.length > 0) {
      productosFiltrados = productosFiltrados.filter(p =>
        p.categoria && this.selectedCategories.includes(p.categoria.idCategoria)
      );
    }

    if (query) {
      productosFiltrados = productosFiltrados.filter(p =>
        p.nombre.toLowerCase().includes(query.toLowerCase())
      );
    }

    this.filteredProducts = productosFiltrados;
  }

  actualizarFiltroCategorias(categoriaId: number, checked: boolean): void {
    if (checked) {
      this.selectedCategories.push(categoriaId);
    } else {
      this.selectedCategories = this.selectedCategories.filter(id => id !== categoriaId);
    }
    this.filtrarProductos('');
  }

  obtenerImagenUrl(imagenUrl?: string): string {
    return imagenUrl ? this.productService.obtenerImagenUrl(imagenUrl) : 'assets/no-image.png';
  }

  comprarProducto(producto: Producto): void {
    this.cartService.addToCart(producto);
  }

  ngOnDestroy() {
    this.searchSubscription.unsubscribe();
  }
}

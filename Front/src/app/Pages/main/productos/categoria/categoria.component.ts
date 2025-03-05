import {Component, OnInit} from '@angular/core';
import {Categoria, Producto} from '../../../../interfaces/global.interfaces';
import {CategoriaService} from '../../../../services/categoria.service';
import {FormsModule} from '@angular/forms';
import {NgForOf} from '@angular/common';
import {ProductService} from '../../../../services/product.service';

@Component({
  selector: 'app-categoria',
  imports: [
    FormsModule,
    NgForOf
  ],
  templateUrl: './categoria.component.html',
  styles: ``
})
export class CategoriaComponent implements OnInit {
  categorias: Categoria[] = [];
  nuevaCategoria: string = '';
  productosDisponibles: Producto[] = [];
  productoSeleccionado: number | null = null;
  categoriasSeleccionadas: number[] = [];
  constructor(private categoriaService: CategoriaService, private productoService: ProductService) {}

  ngOnInit(): void {
    this.cargarCategorias();
    this.cargarProductos();
  }
  cargarProductos(): void {
    this.productoService.getProductos().subscribe(
      (data) => (this.productosDisponibles = data),
      (error) => console.error('Error al obtener productos', error)
    );
  }
  cargarCategorias(): void {
    this.categoriaService.getCategorias().subscribe(
      (data) => (this.categorias = data),
      (error) => console.error('Error al obtener categorías', error)
    );
  }

  agregarCategoria(): void {
    if (!this.nuevaCategoria.trim()) return;
    const categoria: Categoria = { idCategoria: 0, nombreCategoria: this.nuevaCategoria };
    this.categoriaService.crearCategoria(categoria).subscribe(() => {
      this.nuevaCategoria = '';
      this.cargarCategorias();
    });
  }

  eliminarCategoria(id: number): void {
    this.categoriaService.eliminarCategoria(id).subscribe(() => this.cargarCategorias());
  }

  asignarCategoriasAProducto(): void {
    if (!this.productoSeleccionado) return;
    this.productoService.asignarCategoriasAProducto(this.productoSeleccionado, this.categoriasSeleccionadas).subscribe(() => {
      this.categoriasSeleccionadas = [];
      alert('Categorías asignadas correctamente al producto');
    });
  }
}


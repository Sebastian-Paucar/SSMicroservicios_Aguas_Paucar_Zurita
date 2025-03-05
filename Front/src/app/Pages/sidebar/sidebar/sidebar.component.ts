import { Component, OnInit } from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NgForOf} from '@angular/common';
import {CategoriaService} from '../../../services/categoria.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  imports: [
    FormsModule,
    NgForOf
  ],
  styles: ``
})
export class SidebarComponent implements OnInit {
  categories: { name: string, selected: boolean }[] = [];

  constructor(private categoriaService: CategoriaService) {}

  ngOnInit(): void {
    this.categoriaService.getCategorias().subscribe(categorias => {
      this.categories = categorias.map(c => ({ name: c.nombreCategoria, selected: false }));
    });
  }

  applyFilters() {
    const selectedCategories = this.categories.filter(c => c.selected).map(c => c.name);
    console.log('Filtrando por categorías:', selectedCategories);
  }
}

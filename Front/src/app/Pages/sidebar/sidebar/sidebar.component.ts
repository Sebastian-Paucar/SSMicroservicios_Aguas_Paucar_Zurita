import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-sidebar',
  imports: [
    FormsModule
  ],
  templateUrl: './sidebar.component.html',
  styles: ``
})
export class SidebarComponent {
  categories = [
    { name: 'Electrónica', selected: false },
    { name: 'Hogar', selected: false },
    { name: 'Deportes', selected: false },
    { name: 'Ropa', selected: false }
  ];
  providers = ['Proveedor A', 'Proveedor B', 'Proveedor C'];
  selectedProvider: string = '';

  applyFilters() {
    const selectedCategories = this.categories.filter(c => c.selected).map(c => c.name);
    console.log('Filtrando por categorías:', selectedCategories, 'Proveedor:', this.selectedProvider);
  }
}

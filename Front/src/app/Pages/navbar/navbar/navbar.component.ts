import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-navbar',
  imports: [
    FormsModule
  ],
  templateUrl: './navbar.component.html',
  styles: ``
})
export class NavbarComponent {

  searchQuery: string = '';
  filteredResults: string[] = [];
  showCart: boolean = false;
  showUserMenu: boolean = false;
  cartItems = [
    { name: 'Producto 1', price: 20 },
    { name: 'Producto 2', price: 35 }
  ];
  isLoggedIn: boolean = false;

  filterResults() {
    const mockResults = ['Producto A', 'Producto B', 'Producto C'];
    this.filteredResults = this.searchQuery ? mockResults.filter(r => r.toLowerCase().includes(this.searchQuery.toLowerCase())) : [];
  }

  selectResult(result: string) {
    this.searchQuery = result;
    this.filteredResults = [];
  }

  toggleCart() {
    this.showCart = !this.showCart;
  }

  goToCart() {
    alert('Ir al carrito');
  }

  goToShipments() {
    alert('Ir a envíos');
  }

  toggleUserMenu() {
    this.showUserMenu = !this.showUserMenu;
  }

  login() {
    this.isLoggedIn = true;
    this.showUserMenu = false;
  }

  logout() {
    this.isLoggedIn = false;
    this.showUserMenu = false;
  }

  goToProfile() {
    alert('Ir al perfil');
  }

  goToSettings() {
    alert('Ir a configuración');
  }
}


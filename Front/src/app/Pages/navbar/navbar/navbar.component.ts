import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TokenService } from '../../../services/token.service';
import { Router, NavigationEnd } from '@angular/router';
import { SearchService } from '../../../services/search.service';
import { CartService } from '../../../services/cart.service';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-navbar',
  imports: [ FormsModule ],
  templateUrl: './navbar.component.html',
  styles: ``
})
export class NavbarComponent implements OnInit {
  searchQuery: string = '';
  filteredResults: string[] = [];

  showUserMenu: boolean = false;
  cartItems: { producto: any; cantidad: number }[] = [];
  isLoggedIn: boolean = false;
  isCartView: boolean = false;  // Variable para verificar si estamos en el carrito

  constructor(
    private tokenService: TokenService,
    private router: Router,
    private searchService: SearchService,
    private cartService: CartService
  ) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      this.isCartView = ['/carrito', '/register'].some(path => event.url.includes(path));
    });
  }

  ngOnInit(): void {
    if (this.tokenService.getAccessToken()) {
      this.isLoggedIn = true;
      this.showUserMenu = false;
    }

    this.cartService.cartItems$.subscribe(items => {
      this.cartItems = items;
    });
  }

  filterResults() {
    this.searchService.setSearchQuery(this.searchQuery);
  }

  selectResult(result: string) {
    this.searchQuery = result;
    this.searchService.setSearchQuery(result);
  }

  goToCart() {
    this.router.navigate(['/carrito']).then(r => console.log(r));
  }

  login() {
    if (!this.tokenService.getAccessToken()) {
      this.router.navigate(['/login']).then(r => console.log(r));
      return;
    }
  }

  logout() {
    this.router.navigate(['/logout']).then(r => console.log(r));
    this.isLoggedIn = false;
    this.showUserMenu = false;
  }

  navigateTo(url: string) {
    this.router.navigate([url]).then(r => console.log(r));
  }

  goToShipments() {

  }
}

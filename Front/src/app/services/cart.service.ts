import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Producto } from '../interfaces/global.interfaces';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartItems: { producto: Producto; cantidad: number }[] = [];
  private cartItemsSubject = new BehaviorSubject<{ producto: Producto; cantidad: number }[]>([]);
  cartItems$ = this.cartItemsSubject.asObservable();

  constructor() {
    this.loadCartFromStorage(); // Cargar carrito desde LocalStorage al iniciar
  }

  /**
   * Agregar un producto al carrito.
   */
  addToCart(producto: Producto) {
    const existingItem = this.cartItems.find(item => item.producto.idProducto === producto.idProducto);

    if (existingItem) {
      existingItem.cantidad++;
    } else {
      this.cartItems.push({ producto, cantidad: 1 });
    }

    this.updateCartState();
  }

  /**
   * Actualizar la cantidad de un producto.
   */
  updateQuantity(idProducto: number, cantidad: number) {
    const item = this.cartItems.find(i => i.producto.idProducto === idProducto);
    if (item) {
      item.cantidad = cantidad;
      this.updateCartState();
    }
  }

  /**
   * Eliminar un producto del carrito.
   */
  removeFromCart(idProducto: number) {
    this.cartItems = this.cartItems.filter(i => i.producto.idProducto !== idProducto);
    this.updateCartState();
  }

  /**
   * Vaciar el carrito completamente.
   */
  clearCart() {
    this.cartItems = [];
    this.updateCartState();
  }

  /**
   * Guardar el estado del carrito en LocalStorage.
   */
  private updateCartState() {
    localStorage.setItem('cart', JSON.stringify(this.cartItems));
    this.cartItemsSubject.next([...this.cartItems]);
  }

  /**
   * Cargar el carrito desde LocalStorage cuando se inicia la aplicación.
   */
  private loadCartFromStorage() {
    const storedCart = localStorage.getItem('cart');
    if (storedCart) {
      this.cartItems = JSON.parse(storedCart);
      this.cartItemsSubject.next([...this.cartItems]);
    }
  }
}

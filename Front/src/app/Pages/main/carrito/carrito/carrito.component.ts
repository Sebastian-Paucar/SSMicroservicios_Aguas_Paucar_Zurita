import { Component, OnInit } from '@angular/core';
import {CartService} from '../../../../services/cart.service';
import {ProductService} from '../../../../services/product.service';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {NavbarComponent} from '../../../navbar/navbar/navbar.component';


@Component({
  selector: 'app-carrito',
  templateUrl: './carrito.component.html',
  imports: [
    FormsModule,
    NavbarComponent
  ],
  styles: ``
})
export class CarritoComponent implements OnInit {
  cartItems: { producto: any; cantidad: number }[] = [];
  total = 0;

  constructor(private cartService: CartService, protected productService: ProductService,private router: Router) { }

  ngOnInit(): void {
    this.cartService.cartItems$.subscribe(items => {
      this.cartItems = items;
      this.calcularTotal();
    });
  }


  calcularTotal() {
    this.total = parseFloat(
      this.cartItems.reduce((acc, item) => acc + item.producto.precio * item.cantidad, 0).toFixed(2)
    );
  }


  updateQuantity(idProducto: number, cantidad: number) {
    this.cartService.updateQuantity(idProducto, cantidad);
    this.calcularTotal();
  }

  eliminarProducto(idProducto: number) {
    this.cartService.removeFromCart(idProducto);
    this.calcularTotal();
  }

  comprar() {
    const token = localStorage.getItem('access_token');

    if (!token) {
      // Guardar la URL antes de redirigir al login
      localStorage.setItem('redirectAfterLogin', '/carrito');
      this.router.navigate(['/login']);
      return;
    }

    // Verificar si hay productos en el carrito
    if (this.cartItems.length === 0) {
      alert("No hay productos en el carrito.");
      return;
    }

    let compraExitosa = true;

    // Iterar sobre los productos en el carrito
    this.cartItems.forEach(item => {
      this.productService.getProducto(item.producto.idProducto).subscribe({
        next: (producto) => {
          if (producto.cantidad >= item.cantidad) {
            const nuevaCantidad = producto.cantidad - item.cantidad;

            // Actualizar la cantidad en el backend
            this.productService.actualizarCantidad(item.producto.idProducto, nuevaCantidad).subscribe({
              next: () => {
                console.log(`Cantidad actualizada para ${producto.nombre}`);
              },
              error: () => {
                alert(`Error al actualizar stock para ${producto.nombre}`);
                compraExitosa = false;
              }
            });
          } else {
            alert(`No hay suficiente stock para ${item.producto.nombre}`);
            compraExitosa = false;
          }
        },
        error: () => {
          alert(`Error al obtener información del producto ${item.producto.nombre}`);
          compraExitosa = false;
        }
      });
    });

    // Limpiar carrito solo si todas las compras fueron exitosas
    setTimeout(() => {
      if (compraExitosa) {
        this.cartService.clearCart();
        alert("Compra realizada con éxito");
      }
    }, 500);
  }



}

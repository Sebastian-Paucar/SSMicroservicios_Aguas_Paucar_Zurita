import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor() { }

  getProducts() {
    return [
      { id: 1, name: 'Producto 1', price: 20, image: 'https://picsum.photos/200/300?random=1' },
      { id: 2, name: 'Producto 2', price: 30, image: 'https://picsum.photos/200/300?random=2' },
      { id: 3, name: 'Producto 3', price: 40, image: 'https://picsum.photos/200/300?random=3' },
      { id: 4, name: 'Producto 4', price: 50, image: 'https://picsum.photos/200/300?random=4' },
    ];
  }
}

import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {Producto} from '../interfaces/global.interfaces';

@Injectable({
  providedIn: 'root'
})

export class ProductService {
  private endpoint: string = environment.endpointFree; // URL base del backend
  private apiUrl: string = `${this.endpoint}api/productos`; // Endpoint para productos

  constructor(private http: HttpClient) {}

  /**
   * Construir los headers con el JWT desde el Local Storage.
   */
  private getAuthHeaders(): HttpHeaders {
    const token: string | null = localStorage.getItem('access_token');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  /**
   * Manejo de errores en las peticiones HTTP.
   */
  private handleError(error: any): Observable<never> {
    console.error('Error en la petición:', error);
    return throwError(() => new Error(error.message || 'Error en el servidor'));
  }

  /**
   * Obtener la lista de productos.
   */
  getProductos(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.apiUrl)
      .pipe(catchError(this.handleError));
  }

  /**
   * Obtener un producto por su ID.
   */
  getProducto(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError));
  }

  /**
   * Crear un nuevo producto con imagen opcional.
   */
  crearProducto(producto: Producto, imagen?: File): Observable<Producto> {
    const formData = new FormData();
    formData.append('producto', new Blob([JSON.stringify(producto)], { type: 'application/json' }));
    if (imagen) {
      formData.append('file', imagen);
    }

    const emailUsuario = "juan.perez@example.com";
      //localStorage.getItem('email'); // Obtener el email del usuario autenticado
    formData.append('emailUsuario', emailUsuario || '');

    return this.http.post<Producto>(this.apiUrl, formData)
      .pipe(catchError(this.handleError));
  }

  /**
   * Eliminar un producto por su ID.
   */
  eliminarProducto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError));
  }

  /**
   * Obtener la URL de la imagen para mostrar en el frontend.
   */
  /**
   * Obtener la URL de la imagen para mostrar en el frontend.
   */
  obtenerImagenUrl(imagenPath: string): string {
    if (!imagenPath) {
      return 'assets/no-image.png'; // Imagen por defecto si no hay imagen
    }

    // Enviar la ruta como parámetro en la solicitud al backend
    return `${this.endpoint}/api/productos/imagenes?path=${encodeURIComponent(imagenPath)}`;
  }

  /**
   * Actualizar la cantidad de un producto en el backend.
   */
  actualizarCantidad(id: number, cantidad: number): Observable<Producto> {
    return this.http.patch<Producto>(`${this.apiUrl}/${id}/cantidad?cantidad=${cantidad}`, {}, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError));
  }


}

import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {Categoria, Producto} from "../interfaces/global.interfaces";

@Injectable({
  providedIn: 'root'
})
export class CategoriaService {
  private apiUrl = `${environment.endpointFree}api/categorias`;

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('access_token');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  getCategorias(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(this.apiUrl).pipe(catchError(this.handleError));
  }

  getCategoria(id: number): Observable<Categoria> {
    return this.http.get<Categoria>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() })
        .pipe(catchError(this.handleError));
  }

  crearCategoria(categoria: Categoria): Observable<Categoria> {
    return this.http.post<Categoria>(this.apiUrl, categoria, { headers: this.getAuthHeaders() })
        .pipe(catchError(this.handleError));
  }

  actualizarCategoria(id: number, categoria: Categoria): Observable<Categoria> {
    return this.http.put<Categoria>(`${this.apiUrl}/${id}`, categoria, { headers: this.getAuthHeaders() })
        .pipe(catchError(this.handleError));
  }

  eliminarCategoria(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() })
        .pipe(catchError(this.handleError));
  }

  obtenerProductosDeCategoria(id: number): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.apiUrl}/${id}/productos`, { headers: this.getAuthHeaders() })
        .pipe(catchError(this.handleError));
  }



  private handleError(error: any): Observable<never> {
    console.error('Error en la petición:', error);
    return throwError(() => new Error(error.message || 'Error en el servidor'));
  }
}

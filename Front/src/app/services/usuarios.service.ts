import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { Role, Usuario } from '../interfaces/global.interfaces';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UsuariosService {

  private endpoint: string = environment.endpointUsers; // Usa el endpoint desde el entorno
  private apiUrl: string = `${this.endpoint}usuarios`; // Construye la URL de usuarios
  private apiUrlCreate: string = `${this.endpoint}crear-usuario`; // Construye la URL para crear usuario

  constructor(private http: HttpClient) {}

  /**
   * Build HTTP Authorization headers for bearer token.
   */
  private getAuthHeaders(): HttpHeaders {
    const token: string | null = localStorage.getItem('access_token');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  /**
   * Handle errors in a consistent way for all HTTP calls.
   */
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Client-side error: ${error.error.message}`;
    } else {
      errorMessage = `Server returned code: ${error.status}. Message: ${error.message}`;
    }
    return throwError(() => new Error(errorMessage));
  }

  /**
   * GET list of all Usuarios.
   */
  getListUsuarios(): Observable<Usuario[]> {
    return this.http
      .get<Usuario[]>(this.apiUrl, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError));
  }

  /**
   * GET list of all Roles.
   */
  getListRoles(): Observable<Role[]> {
    return this.http
      .get<Role[]>(`${this.apiUrl}/roles`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError));
  }

  /**
   * GET a single Usuario by ID.
   */
  getUsuario(id: number): Observable<Usuario> {
    return this.http
      .get<Usuario>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError));
  }

  /**
   * POST a new Usuario.
   */
  agregarUsuario(usuario: Usuario): Observable<Usuario> {
    return this.http
      .post<Usuario>(this.apiUrlCreate, usuario) // Usa la URL configurada en `environment.ts`
      .pipe(catchError(this.handleError));
  }

  /**
   * PUT (update) an existing Usuario by ID.
   */
  actualizarUsuario(id: number, usuario: Usuario): Observable<Usuario> {
    return this.http
      .put<Usuario>(`${this.apiUrl}/${id}`, usuario, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError));
  }

  /**
   * DELETE a Usuario by ID.
   */
  eliminarUsuario(id: number): Observable<Usuario> {
    return this.http
      .delete<Usuario>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError));
  }
}

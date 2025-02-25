import { CanActivate, Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { TokenService } from './token.service';

/**
 * Guard que verifica la existencia de un token de acceso.
 * Si no se encuentra, redirige al usuario a la página de login.
 */
@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  /**
   * Constructor que inyecta los servicios necesarios.
   *
   * @param tokenService Servicio que gestiona y recupera el token de acceso.
   * @param router Router de Angular para redirecciones.
   */
  constructor(
    private tokenService: TokenService,
    private router: Router
  ) {}

  /**
   * Método encargado de verificar si la ruta puede activarse.
   *
   * @returns Promesa que resuelve en `true` si el token existe, o `false` en caso contrario.
   */
  async canActivate(): Promise<boolean> {
    // Se introduce un pequeño retardo antes de la verificación (opcional).
    await this.cooldown(200);

    // Si no se encuentra un token, se redirige al usuario al login.
    if (!this.tokenService.getAccessToken()) {
      this.router.navigate(['/login']);
      return false;
    }

    // Si existe el token, se permite el acceso.
    return true;
  }

  /**
   * Función de utilidad para generar un retraso.
   *
   * @param ms Cantidad de milisegundos a esperar.
   * @returns Promesa que se resuelve después del tiempo especificado.
   */
  private cooldown(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
}

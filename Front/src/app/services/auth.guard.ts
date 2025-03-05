import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { TokenService } from './token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private tokenService: TokenService,
    private router: Router
  ) {}

  async canActivate(route: ActivatedRouteSnapshot): Promise<boolean> {
    // Pequeño retardo opcional
    await this.cooldown(200);

    const token = this.tokenService.getAccessToken();
    if (!token) {
      this.router.navigate(['/login']);
      return false;
    }

    const userRoles = this.tokenService.getUserRoles();
    const requiredRoles: string[] = route.data['roles']; // Obtener roles requeridos desde la configuración de la ruta

    // Si la ruta tiene roles definidos, verificamos si el usuario tiene al menos uno de ellos
    if (requiredRoles && requiredRoles.length > 0) {
      const hasPermission = userRoles.some(role => requiredRoles.includes(role));

      if (!hasPermission) {
        this.router.navigate(['/dashboard']); // Redirigir si no tiene permisos
        return false;
      }
    }

    return true;
  }

  private cooldown(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
}

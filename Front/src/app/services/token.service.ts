import {Injectable} from '@angular/core';

const  ACCESS_TOKEN='access_token';
const REFRESH_TOKEN='refresh_token';
@Injectable({
  providedIn: 'root'
})
export class TokenService {

  constructor() { }
  setToken(access_token: string, refresh_token: string): void {
    localStorage.removeItem(ACCESS_TOKEN);
    localStorage.setItem(ACCESS_TOKEN, access_token);
    localStorage.removeItem(REFRESH_TOKEN);
    localStorage.setItem(REFRESH_TOKEN, refresh_token);
  }
  clearToken(): void {
    localStorage.removeItem(ACCESS_TOKEN);
    localStorage.removeItem(REFRESH_TOKEN);
  }

  getAccessToken() {
    return localStorage.getItem(ACCESS_TOKEN);
  }

  getRefreshToken(): string {
    return localStorage.getItem(REFRESH_TOKEN) || '';
  }
  /**
   * OBTIENE el rol del Usuario
   */


  getUserRoles(): string[] {
    const token = this.getAccessToken();
    if (!token) {
      // console.warn("JWT no encontrado en localStorage");
      return [];
    }

    try {
      const base64Url = token.split('.')[1];
      if (!base64Url) {
        // console.warn("Formato de JWT inválido");
        return [];
      }

      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = JSON.parse(atob(base64));

      // console.log("Payload del JWT:", jsonPayload);

      // Extraemos los roles desde 'roles'
      const roles = jsonPayload.roles || [];

      // console.log("Roles extraídos del JWT:", roles); // ✅ Depuració

      return roles;
    } catch (error) {
      // console.error("Error al decodificar el JWT", error);
      return [];
    }
  }
}

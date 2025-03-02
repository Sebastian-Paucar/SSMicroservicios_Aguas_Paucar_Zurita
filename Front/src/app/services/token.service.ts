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
}

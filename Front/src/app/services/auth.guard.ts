import { CanActivate, Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { TokenService } from './token.service';

@Injectable({
  providedIn: 'root'
})
export class authGuard implements CanActivate {

  constructor(private tokenService: TokenService, private router: Router) {}

  async canActivate(): Promise<boolean> {
    await this.cooldown(200);
    if (!this.tokenService.getAccessToken()) {
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }

  private cooldown(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

}

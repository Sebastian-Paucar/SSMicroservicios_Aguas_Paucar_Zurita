import {Component, OnInit} from '@angular/core';
import {environment} from '../../../../environments/environment';
import {ActivatedRoute, Router} from '@angular/router';
import {TokenService} from '../../../services/token.service';
import {AuthService} from '../../../services/auth.service';
import {HttpParams} from '@angular/common/http';

@Component({
  selector: 'app-login',
  imports: [],
  templateUrl: './login.component.html',
  styles: ``
})
export class LoginComponent implements OnInit{
  authorize_uri = environment.authorize_uri;
  params: any = {
    client_id: environment.client_id,
    redirect_uri: environment.redirect_uri,
    scope: environment.scope,
    response_type: environment.response_type,
    response_mode: environment.response_mode,
    code_challenge_method: environment.code_challenge_method,
    code_challenge: environment.code_challenge,
  };

  code = '';

  constructor(
    private tokenService: TokenService,
    private router: Router,
    private authService: AuthService,
    private activatedRoute: ActivatedRoute
  ) {}

  async ngOnInit(): Promise<void> {
    if (this.tokenService.getAccessToken()) {
      // Si ya hay un token, redirige directamente al menú
      this.router.navigate(['/dashboard']);
    } else {
      // Espera un cooldown y verifica si hay un código de autorización en la URL
      await this.cooldown(200);

      this.activatedRoute.queryParams.subscribe(params => {
        this.code = params['code'];
        if (this.code) {
          // Si hay un código, intenta obtener el token
          this.getToken();
        } else {
          // Si no hay código y no hay token, inicia el proceso de login
          this.onLogin();
        }
      });
    }
  }

  onLogin(): void {
    const httpParams = new HttpParams({ fromObject: this.params });
    const codeUrl = `${this.authorize_uri}${httpParams.toString()}`;
    location.href = codeUrl; // Redirige al URL de autorización
  }

  getToken(): void {
    this.authService.getToken(this.code).subscribe(
      data => {
        this.tokenService.setToken(data.access_token, data.refresh_token);

        this.cooldown(200).then(() => {
          // Verificar si hay una URL de redirección almacenada
          const redirectUrl = localStorage.getItem('redirectAfterLogin') || '/dashboard';
          localStorage.removeItem('redirectAfterLogin'); // Eliminar para evitar futuras redirecciones incorrectas

          this.router.navigate([redirectUrl]); // Redirigir al carrito o dashboard
        });
      },
      error => {
        console.error('Error obteniendo el token:', error);
        this.router.navigate(['/error']);
      }
    );
  }


  private cooldown(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

}

import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {TokenService} from '../../../services/token.service';
import {environment} from '../../../../environments/environment';

@Component({
  selector: 'app-logout',
  imports: [],
  templateUrl: './logout.component.html',
  styles: ``
})
export class LogoutComponent implements OnInit {
  constructor(private router: Router, private tokenService: TokenService) {
  }
  ngOnInit(): void {
    const token = localStorage.getItem('access_token');
    if (token) {
      this.tokenService.clearToken();
      this.cooldown(200).then(() => {
        location.href = environment.logout_url;
      });
    } else {
      this.router.navigate(['/login']);
    }
  }
  // Función de enfriamiento que devuelve una promesa
  private cooldown(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
}

import {Component, Input} from '@angular/core';
import {ActivatedRoute, Route, Router, RouterLink, RouterOutlet} from '@angular/router';


@Component({
  selector: 'app-sidebar-admin',
  imports: [
    RouterLink,
  ],
  templateUrl: './sidebar-admin.component.html',
  styleUrl: './sidebar-admin.component.css'
})
export class SidebarAdminComponent {
  @Input() role: string = '';
  menuLinks: { name: string; path: string }[] = [];

  constructor(private router: Router, private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.routeConfig?.children?.forEach((route: Route) => {
      const title = route.data?.['title']; // Corregido
      if (route.path && title) {
        this.menuLinks.push({
          name: title,
          path: `${this.router.url}/${route.path}`
        });
      }
    });

    // console.log("Rutas generadas:", this.menuLinks); // Para depuración
  }

  logout() {
    localStorage.removeItem('access_token');
    this.router.navigate(['/login']);
  }
}

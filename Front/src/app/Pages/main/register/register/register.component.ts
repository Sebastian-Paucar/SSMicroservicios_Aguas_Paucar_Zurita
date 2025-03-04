import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {UsuariosService} from '../../../../services/usuarios.service';
import {Role, Usuario} from '../../../../interfaces/global.interfaces';
import {NgIf} from '@angular/common';
import {Router} from '@angular/router';
import {NavbarComponent} from '../../../navbar/navbar/navbar.component';



@Component({
  selector: 'app-register',
  imports: [
    ReactiveFormsModule,
    NgIf,
    NavbarComponent
  ],
  templateUrl: './register.component.html'

})
export class RegisterComponent {
  registrationForm: FormGroup;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private usuariosService: UsuariosService,
    private router: Router
  ) {
    this.registrationForm = this.fb.group({
      nombre: [
        '',
        [
          Validators.required,
          Validators.pattern('^[a-zA-ZÀ-ÿ\u00f1\u00d1 ]+$')
        ]
      ],
      // Solo dominios .com, .edu.ec, .ec, .net (puedes añadir más)
      email: [
        '',
        [
          Validators.required,
          Validators.pattern(
            // Patrón de ejemplo para permitir varios dominios (ajusta según necesites)
            '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|edu\\.ec|ec|net)$'
          )
        ]
      ],
      // Contraseña: al menos 8 caracteres con mayúsculas, minúsculas, dígitos y un carácter especial
      password: [
        '',
        [
          Validators.required,
          Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$')
        ]
      ],
      telefono: [
        '',
        [
          Validators.required,
          Validators.minLength(10),
          Validators.maxLength(10)
        ]
      ],
      perfil: [
        '',
        [
          Validators.required,
          Validators.pattern('^[a-zA-ZÀ-ÿ\u00f1\u00d1 ]+$')
        ]
      ],
    });
  }

  onSubmit(): void {
    // Limpiar mensajes previos
    this.errorMessage = null;
    this.successMessage = null;

    if (this.registrationForm.valid) {
      // Construimos el objeto de usuario con el rol por defecto "ROLE_USER"
      const nuevoUsuario: Usuario = {
        nombre: this.registrationForm.value.nombre,
        email: this.registrationForm.value.email,
        password: this.registrationForm.value.password,
        telefono: this.registrationForm.value.telefono,
        perfil: this.registrationForm.value.perfil,
        imagenPerfil: '', // Ajusta si tu backend requiere manejar imágenes
        roles: [
          {
            id: 0,           // si el backend lo autogenera, puede ser 0
            role: 'ROLE_USER',
            authority: 'ROLE_USER'// Valor por defecto
          } as Role
        ],
        direcciones: []
      };

      this.usuariosService.agregarUsuario(nuevoUsuario).subscribe({
        next: () => {
          this.successMessage = 'Usuario registrado con éxito.';
          this.registrationForm.reset();
          setTimeout(() => {
            this.router.navigate(['/dashboard']); // Redirigir al dashboard
          }, 2000);
        },
        error: (err) => {
          // Mensaje de error proveniente de la respuesta o un fallback
          this.errorMessage = err.message || 'Error al registrar el usuario.';
        }
      });
    } else {
      // Marcar todos los controles como tocados para mostrar errores
      this.registrationForm.markAllAsTouched();
    }
  }
}

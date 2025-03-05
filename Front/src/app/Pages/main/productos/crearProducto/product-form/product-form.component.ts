import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { ProductService } from '../../../../../services/product.service';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {CategoriaComponent} from '../../categoria/categoria.component';



@Component({
  selector: 'app-product-form',
  standalone: true,
  templateUrl: './product-form.component.html',
  imports: [
    ReactiveFormsModule,
    MatSnackBarModule,

  ],
  styles: ``
})
export class ProductFormComponent {
  productForm: FormGroup;
  imagen?: File;
  previewUrl?: string;
  imagenError: string = '';

  private maxFileSize = 5242880; // 5MB en bytes
  private allowedFileTypes = ['image/png', 'image/jpeg', 'image/jpg', 'image/webp'];

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private sanitizer: DomSanitizer,
    private snackBar: MatSnackBar // Agregamos el servicio MatSnackBar
  ) {
    this.productForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100), this.noHtmlValidator]],
      descripcion: ['', [Validators.required, Validators.maxLength(500), this.noHtmlValidator]],
      precio: [0, [Validators.required, Validators.min(0)]],
      cantidad: [1, [Validators.required, Validators.min(1)]],
      estado: ['DISPONIBLE', Validators.required]
    });
  }

  /**
   * Validador personalizado para evitar inyecciones de código malicioso.
   */
  noHtmlValidator(control: any) {
    const regex = /<\/?[a-z][\s\S]*>/i;
    return regex.test(control.value) ? { invalidHtml: true } : null;
  }

  /**
   * Maneja la selección de imagen y muestra la vista previa con validaciones.
   */
  onFileSelected(event: any): void {
    const file = event.target.files[0];
    this.imagenError = '';

    if (file) {
      if (!this.allowedFileTypes.includes(file.type)) {
        this.imagenError = 'Solo se permiten archivos de imagen (PNG, JPG, JPEG, WEBP).';
        this.mostrarNotificacion(this.imagenError, 'error');
        return;
      }

      if (file.size > this.maxFileSize) {
        this.imagenError = 'El archivo debe ser menor a 5MB.';
        this.mostrarNotificacion(this.imagenError, 'error');
        return;
      }

      this.imagen = file;

      // Generar vista previa de la imagen
      const reader = new FileReader();
      reader.onload = (e) => this.previewUrl = this.sanitizer.bypassSecurityTrustUrl(e.target?.result as string) as string;
      reader.readAsDataURL(file);
    }
  }

  /**
   * Envia el formulario para crear un producto.
   */
  crearProducto(): void {
    if (this.productForm.invalid || this.imagenError) {
      this.mostrarNotificacion('Formulario inválido, revisa los campos.', 'error');
      return;
    }

    this.productService.crearProducto(this.productForm.value, this.imagen).subscribe({
      next: () => {
        this.mostrarNotificacion('Producto creado correctamente', 'success');
        this.resetForm();
      },
      error: (err) => {
        this.mostrarNotificacion('Error al crear el producto: ' + err.message, 'error');
      }
    });
  }

  /**
   * Resetea el formulario después de enviar.
   */
  private resetForm(): void {
    this.productForm.reset({
      nombre: '',
      descripcion: '',
      precio: 0,
      cantidad: 1,
      estado: 'DISPONIBLE'
    });
    this.imagen = undefined;
    this.previewUrl = undefined;
    this.imagenError = '';
  }

  /**
   * Muestra una notificación con Angular Material Snackbar.
   */
  private mostrarNotificacion(mensaje: string, tipo: 'success' | 'error'): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 3000,
      panelClass: tipo === 'success' ? ['bg-green-500', 'text-white'] : ['bg-red-500', 'text-white']
    });

  }
}

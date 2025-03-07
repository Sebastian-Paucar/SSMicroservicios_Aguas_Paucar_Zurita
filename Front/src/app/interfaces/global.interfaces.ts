export interface Curso {
  id:number;
  nombre: string;
  cursoUsuarios:cursoUsuarios[];
}
export interface CursoRequest {
  idcurso:number,
  usuarios:[]
}
export interface Usuario {
  nombre: string;
  email: string;
  password: string;
  telefono: string;
  perfil: string;
  imagenPerfil?: string;       // si es opcional, marca con '?'
  roles: Role[];
  direcciones?: Direccion[];   // si es opcional, marca con '?'
}
export interface Direccion {
  idDireccion: number;
  callePrincipal: string;
  calleSecundaria: string;
  numero?: string;
  // agrega más campos si tu backend los maneja
}
export interface Role {
  id: number;
  role: string;
}

export interface Authority {
  authority: string;
}

export interface cursoUsuarios {
  "id": number,
  "usuarioId": number,
  'nombre': string,
}
export interface Response<TEntity> {
  Data: TEntity;
  DataList: TEntity[];
  Success: boolean;
  ErrorMessage: string;
  SuccessMessage: string;
}
export interface Categoria {
  idCategoria: number;
  nombreCategoria: string;
}

export interface Producto {
  idProducto?: number;
  nombre: string;
  descripcion: string;
  precio: number;
  cantidad: number;
  estado: string;
  imagenUrl?: string; // URL de la imagen en el backend
  categoria: Categoria; // Relación con la categoría
}

export interface Categoria {
  idCategoria: number;
  nombreCategoria: string;
}

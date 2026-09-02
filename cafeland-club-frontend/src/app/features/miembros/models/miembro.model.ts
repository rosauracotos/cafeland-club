export type EstadoMiembro = 'ACTIVO' | 'INACTIVO';

export interface Miembro {
  id: number;
  nombre: string;
  rangoId: number;
  rangoNombre: string;
  fechaIngreso: string;
  estado: EstadoMiembro;
}

export interface MiembroCrearRequest {
  nombre: string;
  rangoId: number;
  fechaIngreso: string;
  estado: EstadoMiembro;
}

export interface MiembroActualizarRequest {
  nombre: string;
  rangoId: number;
  fechaIngreso: string;
}

export interface EstadoMiembroRequest {
  estado: EstadoMiembro;
}

export interface ResultadoSemanal {
  id: number;
  miembroId: number;
  nombreMiembro: string;
  semanaId: number;
  numeroSemana: number;
  puntosDesafio: number;
  minimoDesafio: number;
  cumpleDesafio: boolean;
  puntosTorneo: number;
  minimoTorneo: number;
  cumpleTorneo: boolean;
}

export interface ResultadoSemanalGuardarRequest {
  miembroId: number;
  semanaId: number;
  puntosDesafio: number;
  puntosTorneo: number;
}

export interface ResultadoSemanalFila {
  resultadoId: number | null;
  miembroId: number;
  nombreMiembro: string;
  puntosDesafio: number;
  puntosTorneo: number;
  cumpleDesafio: boolean | null;
  cumpleTorneo: boolean | null;
}

export interface ResultadoSemanalFilaGuardar {
  resultadoId: number | null;
  miembroId: number;
  puntosDesafio: number;
  puntosTorneo: number;
}

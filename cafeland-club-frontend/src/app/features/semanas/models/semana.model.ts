export interface Semana {
  id: number;
  numeroSemana: number;
  fechaInicio: string;
  fechaFin: string;
  ligaId: number;
  ligaNumero: number;
  minimoPuntosTorneo: number;
}

export interface SemanaGuardarRequest {
  numeroSemana: number;
  fechaInicio: string;
  fechaFin: string;
  ligaId: number;
}

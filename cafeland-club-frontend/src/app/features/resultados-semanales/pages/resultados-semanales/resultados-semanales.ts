import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { MiembroService } from '../../../miembros/services/miembro.service';
import { Semana } from '../../../semanas/models/semana.model';
import { SemanaService } from '../../../semanas/services/semana.service';
import { ResultadosTablaComponent } from '../../components/resultados-tabla/resultados-tabla';
import {
  ResultadoSemanal,
  ResultadoSemanalFila,
  ResultadoSemanalFilaGuardar,
} from '../../models/resultado-semanal.model';
import { ResultadoSemanalService } from '../../services/resultado-semanal.service';

@Component({
  selector: 'app-resultados-semanales',
  imports: [ResultadosTablaComponent],
  templateUrl: './resultados-semanales.html',
  styleUrl: './resultados-semanales.css',
})
export class ResultadosSemanalesComponent implements OnInit {
  private static readonly MINIMO_DESAFIO = 2000;

  private readonly semanaService = inject(SemanaService);
  private readonly miembroService = inject(MiembroService);
  private readonly resultadoService = inject(ResultadoSemanalService);

  protected readonly semanas = signal<Semana[]>([]);
  protected readonly semanaSeleccionada = signal<Semana | null>(null);
  protected readonly filas = signal<ResultadoSemanalFila[]>([]);
  protected readonly cargandoSemanas = signal(true);
  protected readonly cargandoResultados = signal(false);
  protected readonly guardandoMiembroId = signal<number | null>(null);
  protected readonly mensajeError = signal('');
  protected readonly mensajeExito = signal('');
  protected readonly minimoDesafio = ResultadosSemanalesComponent.MINIMO_DESAFIO;

  ngOnInit(): void {
    this.semanaService.listarTodas().subscribe({
      next: (semanas) => {
        this.semanas.set([...semanas].sort((a, b) => a.numeroSemana - b.numeroSemana));
        this.cargandoSemanas.set(false);
      },
      error: (error: HttpErrorResponse) => {
        console.error('No se pudieron cargar las semanas', error);
        this.mensajeError.set('No se pudo conectar con el servidor para cargar las semanas.');
        this.cargandoSemanas.set(false);
      },
    });
  }

  protected seleccionarSemana(valor: string): void {
    const semanaId = Number(valor);
    const semana = this.semanas().find((actual) => actual.id === semanaId) ?? null;
    this.semanaSeleccionada.set(semana);
    this.filas.set([]);
    this.mensajeError.set('');
    this.mensajeExito.set('');

    if (!semana) return;

    this.cargandoResultados.set(true);
    forkJoin({
      miembros: this.miembroService.listarActivos(),
      resultados: this.resultadoService.listarPorSemana(semana.id),
    }).subscribe({
      next: ({ miembros, resultados }) => {
        const porMiembro = new Map(resultados.map((resultado) => [resultado.miembroId, resultado]));
        this.filas.set(miembros.map((miembro) => {
          const resultado = porMiembro.get(miembro.id);
          return {
            resultadoId: resultado?.id ?? null,
            miembroId: miembro.id,
            nombreMiembro: miembro.nombre,
            puntosDesafio: resultado?.puntosDesafio ?? 0,
            puntosTorneo: resultado?.puntosTorneo ?? 0,
            cumpleDesafio: resultado?.cumpleDesafio ?? null,
            cumpleTorneo: resultado?.cumpleTorneo ?? null,
          };
        }));
        this.cargandoResultados.set(false);
      },
      error: (error: HttpErrorResponse) => {
        console.error('No se pudieron cargar los resultados', error);
        this.mensajeError.set(this.obtenerMensajeError(error, 'No se pudieron cargar los resultados de la semana.'));
        this.cargandoResultados.set(false);
      },
    });
  }

  protected guardarResultado(datos: ResultadoSemanalFilaGuardar): void {
    const semana = this.semanaSeleccionada();
    if (!semana) return;

    this.guardandoMiembroId.set(datos.miembroId);
    this.mensajeError.set('');
    this.mensajeExito.set('');

    const request = {
      miembroId: datos.miembroId,
      semanaId: semana.id,
      puntosDesafio: datos.puntosDesafio,
      puntosTorneo: datos.puntosTorneo,
    };
    const operacion = datos.resultadoId === null
      ? this.resultadoService.registrar(request)
      : this.resultadoService.actualizar(datos.resultadoId, request);

    operacion.subscribe({
      next: (guardado) => {
        const filaActualizada = this.convertirAFila(guardado);
        this.filas.update((filas) => filas.map((fila) =>
          fila.miembroId === guardado.miembroId ? filaActualizada : fila
        ));
        this.mensajeExito.set(`Resultados de ${guardado.nombreMiembro} guardados correctamente.`);
        this.guardandoMiembroId.set(null);
      },
      error: (error: HttpErrorResponse) => {
        console.error('No se pudieron guardar los resultados', error);
        this.mensajeError.set(this.obtenerMensajeError(error, 'No se pudo guardar el resultado. Revisa los puntos ingresados.'));
        this.guardandoMiembroId.set(null);
      },
    });
  }

  private convertirAFila(resultado: ResultadoSemanal): ResultadoSemanalFila {
    return {
      resultadoId: resultado.id,
      miembroId: resultado.miembroId,
      nombreMiembro: resultado.nombreMiembro,
      puntosDesafio: resultado.puntosDesafio,
      puntosTorneo: resultado.puntosTorneo,
      cumpleDesafio: resultado.cumpleDesafio,
      cumpleTorneo: resultado.cumpleTorneo,
    };
  }

  private obtenerMensajeError(error: HttpErrorResponse, mensajePredeterminado: string): string {
    if (error.status === 0) return 'No se pudo conectar con el backend.';
    if (typeof error.error === 'object' && error.error !== null) {
      const respuesta = error.error as Record<string, unknown>;
      for (const campo of ['error', 'detail', 'message']) {
        if (typeof respuesta[campo] === 'string' && respuesta[campo]) return respuesta[campo];
      }
    }
    return mensajePredeterminado;
  }
}

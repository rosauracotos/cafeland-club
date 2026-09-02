import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { SemanaFormularioComponent } from '../../components/semana-formulario/semana-formulario';
import { Liga } from '../../models/liga.model';
import { Semana, SemanaGuardarRequest } from '../../models/semana.model';
import { LigaService } from '../../services/liga.service';
import { SemanaService } from '../../services/semana.service';

@Component({
  selector: 'app-semanas-lista',
  imports: [DatePipe, SemanaFormularioComponent],
  templateUrl: './semanas-lista.html',
  styleUrl: './semanas-lista.css',
})
export class SemanasListaComponent implements OnInit {
  private readonly semanaService = inject(SemanaService);
  private readonly ligaService = inject(LigaService);

  protected readonly semanas = signal<Semana[]>([]);
  protected readonly ligas = signal<Liga[]>([]);
  protected readonly cargando = signal(true);
  protected readonly formularioVisible = signal(false);
  protected readonly semanaEnEdicion = signal<Semana | null>(null);
  protected readonly guardando = signal(false);
  protected readonly mensajeError = signal('');
  protected readonly errorFormulario = signal('');
  protected readonly mensajeExito = signal('');

  ngOnInit(): void {
    forkJoin({
      semanas: this.semanaService.listarTodas(),
      ligas: this.ligaService.listarTodas(),
    }).subscribe({
      next: ({ semanas, ligas }) => {
        this.semanas.set(this.ordenarSemanas(semanas));
        this.ligas.set(ligas);
        this.cargando.set(false);
      },
      error: (error: HttpErrorResponse) => {
        console.error('No se pudieron cargar las semanas y las ligas', error);
        this.mensajeError.set('No se pudo conectar con el servidor para cargar los datos.');
        this.cargando.set(false);
      },
    });
  }

  protected mostrarNueva(): void {
    this.limpiarMensajes();
    this.semanaEnEdicion.set(null);
    this.formularioVisible.set(true);
  }

  protected editar(semana: Semana): void {
    this.limpiarMensajes();
    this.semanaEnEdicion.set(semana);
    this.formularioVisible.set(true);
  }

  protected cancelarFormulario(): void {
    this.formularioVisible.set(false);
    this.semanaEnEdicion.set(null);
    this.errorFormulario.set('');
  }

  protected guardar(datos: SemanaGuardarRequest): void {
    this.guardando.set(true);
    this.errorFormulario.set('');
    this.mensajeExito.set('');

    const semanaActual = this.semanaEnEdicion();
    const operacion = semanaActual
      ? this.semanaService.actualizar(semanaActual.id, datos)
      : this.semanaService.registrar(datos);

    operacion.subscribe({
      next: (semanaGuardada) => {
        this.semanas.update((semanas) => {
          const existe = semanas.some((semana) => semana.id === semanaGuardada.id);
          const actualizadas = existe
            ? semanas.map((semana) => semana.id === semanaGuardada.id ? semanaGuardada : semana)
            : [...semanas, semanaGuardada];
          return this.ordenarSemanas(actualizadas);
        });
        this.mensajeExito.set(
          semanaActual ? 'Semana actualizada correctamente.' : 'Semana registrada correctamente.'
        );
        this.guardando.set(false);
        this.cancelarFormulario();
      },
      error: (error: HttpErrorResponse) => {
        console.error('No se pudo guardar la semana', error);
        this.errorFormulario.set(
          this.obtenerMensajeError(error, 'No se pudo guardar la semana. Revisa los datos enviados.')
        );
        this.guardando.set(false);
      },
    });
  }

  private ordenarSemanas(semanas: Semana[]): Semana[] {
    return [...semanas].sort((a, b) => a.numeroSemana - b.numeroSemana);
  }

  private limpiarMensajes(): void {
    this.mensajeError.set('');
    this.errorFormulario.set('');
    this.mensajeExito.set('');
  }

  private obtenerMensajeError(error: HttpErrorResponse, mensajePredeterminado: string): string {
    if (error.status === 0) return 'No se pudo conectar con el backend.';

    if (typeof error.error === 'object' && error.error !== null) {
      const respuesta = error.error as Record<string, unknown>;
      for (const campo of ['error', 'detail', 'message']) {
        if (typeof respuesta[campo] === 'string' && respuesta[campo]) {
          return respuesta[campo];
        }
      }
    }

    return mensajePredeterminado;
  }
}

import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import {
  Miembro,
  MiembroActualizarRequest,
  MiembroCrearRequest,
} from '../../models/miembro.model';
import { Rango } from '../../models/rango.model';
import {
  MiembroFormularioComponent,
  MiembroFormularioDatos,
} from '../../components/miembro-formulario/miembro-formulario';
import { MiembroService } from '../../services/miembro.service';
import { RangoService } from '../../services/rango.service';

@Component({
  selector: 'app-miembros-lista',
  imports: [DatePipe, MiembroFormularioComponent],
  templateUrl: './miembros-lista.html',
  styleUrl: './miembros-lista.css',
})
export class MiembrosListaComponent implements OnInit {
  private readonly miembroService = inject(MiembroService);
  private readonly rangoService = inject(RangoService);

  protected readonly miembros = signal<Miembro[]>([]);
  protected readonly rangos = signal<Rango[]>([]);
  protected readonly cargando = signal(true);
  protected readonly formularioVisible = signal(false);
  protected readonly miembroEnEdicion = signal<Miembro | null>(null);
  protected readonly guardando = signal(false);
  protected readonly procesandoEstadoId = signal<number | null>(null);
  protected readonly mensajeError = signal('');
  protected readonly errorFormulario = signal('');
  protected readonly mensajeExito = signal('');

  ngOnInit(): void {
    forkJoin({
      miembros: this.miembroService.listarTodos(),
      rangos: this.rangoService.listarTodos(),
    }).subscribe({
      next: ({ miembros, rangos }) => {
        this.miembros.set(miembros);
        this.rangos.set(rangos);
        this.cargando.set(false);
      },
      error: (error: HttpErrorResponse) => {
        console.error('No se pudieron cargar los datos iniciales', error);
        this.mensajeError.set('No se pudo conectar con el servidor para cargar los datos.');
        this.cargando.set(false);
      },
    });
  }

  protected mostrarNuevo(): void {
    this.limpiarMensajes();
    this.miembroEnEdicion.set(null);
    this.formularioVisible.set(true);
  }

  protected editar(miembro: Miembro): void {
    this.limpiarMensajes();
    this.miembroEnEdicion.set(miembro);
    this.formularioVisible.set(true);
  }

  protected cancelarFormulario(): void {
    this.formularioVisible.set(false);
    this.miembroEnEdicion.set(null);
    this.errorFormulario.set('');
  }

  protected guardar(datos: MiembroFormularioDatos): void {
    this.guardando.set(true);
    this.errorFormulario.set('');
    this.mensajeExito.set('');

    const miembroActual = this.miembroEnEdicion();
    const operacion = miembroActual
      ? this.miembroService.actualizar(miembroActual.id, datos as MiembroActualizarRequest)
      : this.miembroService.registrar({
          ...datos,
          estado: 'ACTIVO',
        } satisfies MiembroCrearRequest);

    operacion.subscribe({
      next: (miembroGuardado) => {
        this.miembros.update((miembros) => {
          const existe = miembros.some((miembro) => miembro.id === miembroGuardado.id);
          return existe
            ? miembros.map((miembro) =>
                miembro.id === miembroGuardado.id ? miembroGuardado : miembro
              )
            : [...miembros, miembroGuardado];
        });
        this.mensajeExito.set(
          miembroActual ? 'Miembro actualizado correctamente.' : 'Miembro registrado correctamente.'
        );
        this.guardando.set(false);
        this.cancelarFormulario();
      },
      error: (error: HttpErrorResponse) => {
        console.error('No se pudo guardar el miembro', error);
        this.errorFormulario.set(
          this.obtenerMensajeError(error, 'No se pudo guardar el miembro. Revisa los datos enviados.')
        );
        this.guardando.set(false);
      },
    });
  }

  protected cambiarEstado(miembro: Miembro): void {
    const nuevoEstado = miembro.estado === 'ACTIVO' ? 'INACTIVO' : 'ACTIVO';
    this.limpiarMensajes();
    this.procesandoEstadoId.set(miembro.id);

    this.miembroService.cambiarEstado(miembro.id, { estado: nuevoEstado }).subscribe({
      next: (miembroActualizado) => {
        this.miembros.update((miembros) =>
          miembros.map((actual) =>
            actual.id === miembroActualizado.id ? miembroActualizado : actual
          )
        );
        this.mensajeExito.set(`El miembro ahora está ${nuevoEstado}.`);
        this.procesandoEstadoId.set(null);
      },
      error: (error: HttpErrorResponse) => {
        console.error('No se pudo cambiar el estado', error);
        this.mensajeError.set(
          this.obtenerMensajeError(error, 'No se pudo cambiar el estado del miembro.')
        );
        this.procesandoEstadoId.set(null);
      },
    });
  }

  private limpiarMensajes(): void {
    this.mensajeError.set('');
    this.errorFormulario.set('');
    this.mensajeExito.set('');
  }

  private obtenerMensajeError(error: HttpErrorResponse, mensajePredeterminado: string): string {
    if (error.status === 0) {
      return 'No se pudo conectar con el backend.';
    }

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

import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Miembro } from '../../models/miembro.model';
import { Rango } from '../../models/rango.model';

export interface MiembroFormularioDatos {
  nombre: string;
  rangoId: number;
  fechaIngreso: string;
}

@Component({
  selector: 'app-miembro-formulario',
  imports: [ReactiveFormsModule],
  templateUrl: './miembro-formulario.html',
  styleUrl: './miembro-formulario.css',
})
export class MiembroFormularioComponent {
  private readonly formBuilder = inject(FormBuilder);

  @Input() rangos: Rango[] = [];
  @Input() guardando = false;
  @Input() error = '';
  @Output() readonly guardar = new EventEmitter<MiembroFormularioDatos>();
  @Output() readonly cancelar = new EventEmitter<void>();

  protected miembroActual: Miembro | null = null;
  protected readonly formulario = this.formBuilder.nonNullable.group({
    nombre: ['', [Validators.required]],
    rangoId: [0, [Validators.required, Validators.min(1)]],
    fechaIngreso: ['', [Validators.required]],
  });

  @Input()
  set miembro(miembro: Miembro | null) {
    this.miembroActual = miembro;
    this.formulario.reset({
      nombre: miembro?.nombre ?? '',
      rangoId: miembro?.rangoId ?? 0,
      fechaIngreso: miembro?.fechaIngreso ?? '',
    });
  }

  protected enviar(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const datos = this.formulario.getRawValue();
    this.guardar.emit({
      ...datos,
      nombre: datos.nombre.trim(),
    });
  }
}

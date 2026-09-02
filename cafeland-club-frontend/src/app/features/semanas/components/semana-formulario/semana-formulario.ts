import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Liga } from '../../models/liga.model';
import { Semana, SemanaGuardarRequest } from '../../models/semana.model';

@Component({
  selector: 'app-semana-formulario',
  imports: [ReactiveFormsModule],
  templateUrl: './semana-formulario.html',
  styleUrl: './semana-formulario.css',
})
export class SemanaFormularioComponent {
  private readonly formBuilder = inject(FormBuilder);

  @Input() ligas: Liga[] = [];
  @Input() guardando = false;
  @Input() error = '';
  @Output() readonly guardar = new EventEmitter<SemanaGuardarRequest>();
  @Output() readonly cancelar = new EventEmitter<void>();

  protected semanaActual: Semana | null = null;
  protected readonly formulario = this.formBuilder.nonNullable.group({
    numeroSemana: [0, [Validators.required, Validators.min(1)]],
    fechaInicio: ['', [Validators.required]],
    fechaFin: ['', [Validators.required]],
    ligaId: [0, [Validators.required, Validators.min(1)]],
  });

  @Input()
  set semana(semana: Semana | null) {
    this.semanaActual = semana;
    this.formulario.reset({
      numeroSemana: semana?.numeroSemana ?? 0,
      fechaInicio: semana?.fechaInicio ?? '',
      fechaFin: semana?.fechaFin ?? '',
      ligaId: semana?.ligaId ?? 0,
    });
  }

  protected enviar(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const datos = this.formulario.getRawValue();
    if (datos.fechaFin < datos.fechaInicio) {
      this.formulario.controls.fechaFin.setErrors({ periodoInvalido: true });
      this.formulario.controls.fechaFin.markAsTouched();
      return;
    }

    this.guardar.emit(datos);
  }
}

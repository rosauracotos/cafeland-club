import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  ResultadoSemanalFila,
  ResultadoSemanalFilaGuardar,
} from '../../models/resultado-semanal.model';

@Component({
  selector: 'app-resultados-tabla',
  imports: [ReactiveFormsModule],
  templateUrl: './resultados-tabla.html',
  styleUrl: './resultados-tabla.css',
})
export class ResultadosTablaComponent {
  private readonly formBuilder = inject(FormBuilder);

  @Input() guardandoMiembroId: number | null = null;
  @Output() readonly guardar = new EventEmitter<ResultadoSemanalFilaGuardar>();

  protected filasActuales: ResultadoSemanalFila[] = [];
  protected readonly resultados = new FormArray(
    [] as ReturnType<typeof this.crearFilaFormulario>[]
  );

  @Input()
  set filas(filas: ResultadoSemanalFila[]) {
    this.filasActuales = filas;
    this.resultados.clear();
    filas.forEach((fila) => this.resultados.push(this.crearFilaFormulario(fila)));
  }

  protected guardarFila(indice: number): void {
    const control = this.resultados.at(indice);
    if (control.invalid) {
      control.markAllAsTouched();
      return;
    }

    this.guardar.emit({
      resultadoId: this.filasActuales[indice].resultadoId,
      ...control.getRawValue(),
    });
  }

  private crearFilaFormulario(fila: ResultadoSemanalFila) {
    return this.formBuilder.nonNullable.group({
      miembroId: [fila.miembroId],
      puntosDesafio: [fila.puntosDesafio, [Validators.required, Validators.min(0)]],
      puntosTorneo: [fila.puntosTorneo, [Validators.required, Validators.min(0)]],
    });
  }
}

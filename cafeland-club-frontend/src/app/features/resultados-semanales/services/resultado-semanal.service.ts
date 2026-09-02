import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import {
  ResultadoSemanal,
  ResultadoSemanalGuardarRequest,
} from '../models/resultado-semanal.model';

@Injectable({ providedIn: 'root' })
export class ResultadoSemanalService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/resultados-semanales';

  listarPorSemana(semanaId: number): Observable<ResultadoSemanal[]> {
    return this.http.get<ResultadoSemanal[]>(`${this.apiUrl}/semana/${semanaId}`);
  }

  registrar(request: ResultadoSemanalGuardarRequest): Observable<ResultadoSemanal> {
    return this.http.post<ResultadoSemanal>(this.apiUrl, request);
  }

  actualizar(id: number, request: ResultadoSemanalGuardarRequest): Observable<ResultadoSemanal> {
    return this.http.put<ResultadoSemanal>(`${this.apiUrl}/${id}`, request);
  }
}

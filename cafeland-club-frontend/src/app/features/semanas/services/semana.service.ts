import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Semana, SemanaGuardarRequest } from '../models/semana.model';

@Injectable({ providedIn: 'root' })
export class SemanaService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/semanas';

  listarTodas(): Observable<Semana[]> {
    return this.http.get<Semana[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Semana> {
    return this.http.get<Semana>(`${this.apiUrl}/${id}`);
  }

  registrar(request: SemanaGuardarRequest): Observable<Semana> {
    return this.http.post<Semana>(this.apiUrl, request);
  }

  actualizar(id: number, request: SemanaGuardarRequest): Observable<Semana> {
    return this.http.put<Semana>(`${this.apiUrl}/${id}`, request);
  }
}

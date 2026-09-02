import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import {
  EstadoMiembroRequest,
  Miembro,
  MiembroActualizarRequest,
  MiembroCrearRequest,
} from '../models/miembro.model';

@Injectable({ providedIn: 'root' })
export class MiembroService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/miembros';

  listarTodos(): Observable<Miembro[]> {
    return this.http.get<Miembro[]>(this.apiUrl);
  }

  listarActivos(): Observable<Miembro[]> {
    return this.http.get<Miembro[]>(`${this.apiUrl}/activos`);
  }

  registrar(request: MiembroCrearRequest): Observable<Miembro> {
    return this.http.post<Miembro>(this.apiUrl, request);
  }

  actualizar(id: number, request: MiembroActualizarRequest): Observable<Miembro> {
    return this.http.put<Miembro>(`${this.apiUrl}/${id}`, request);
  }

  cambiarEstado(id: number, request: EstadoMiembroRequest): Observable<Miembro> {
    return this.http.put<Miembro>(`${this.apiUrl}/${id}/estado`, request);
  }
}

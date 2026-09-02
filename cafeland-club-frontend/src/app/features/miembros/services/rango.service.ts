import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Rango } from '../models/rango.model';

@Injectable({ providedIn: 'root' })
export class RangoService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/rangos';

  listarTodos(): Observable<Rango[]> {
    return this.http.get<Rango[]>(this.apiUrl);
  }
}

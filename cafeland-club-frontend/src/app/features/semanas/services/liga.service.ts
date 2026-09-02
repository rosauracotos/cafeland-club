import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Liga } from '../models/liga.model';

@Injectable({ providedIn: 'root' })
export class LigaService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/ligas';

  listarTodas(): Observable<Liga[]> {
    return this.http.get<Liga[]>(this.apiUrl);
  }
}

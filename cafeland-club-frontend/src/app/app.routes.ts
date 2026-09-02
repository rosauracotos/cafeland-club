import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    loadComponent: () =>
      import('./features/inicio/pages/inicio/inicio').then(
        (component) => component.InicioComponent
      ),
  },
  {
    path: 'miembros',
    loadComponent: () =>
      import('./features/miembros/pages/miembros-lista/miembros-lista').then(
        (component) => component.MiembrosListaComponent
      ),
  },
  {
    path: 'semanas',
    loadComponent: () =>
      import('./features/semanas/pages/semanas-lista/semanas-lista').then(
        (component) => component.SemanasListaComponent
      ),
  },
  {
    path: 'resultados-semanales',
    loadComponent: () =>
      import('./features/resultados-semanales/pages/resultados-semanales/resultados-semanales').then(
        (component) => component.ResultadosSemanalesComponent
      ),
  },
];

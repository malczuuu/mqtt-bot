import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'broker',
    loadComponent: () => import('./pages/broker/broker').then(m => m.Broker)
  },
  {
    path: 'settings',
    loadComponent: () => import('./pages/settings/settings').then(m => m.Settings)
  },
  {
    path: '',
    redirectTo: 'broker',
    pathMatch: 'full'
  }
];

import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DomaineResolve from './route/domaine-routing-resolve.service';

const domaineRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/domaine.component').then(m => m.DomaineComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/domaine-detail.component').then(m => m.DomaineDetailComponent),
    resolve: {
      domaine: DomaineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/domaine-update.component').then(m => m.DomaineUpdateComponent),
    resolve: {
      domaine: DomaineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/domaine-update.component').then(m => m.DomaineUpdateComponent),
    resolve: {
      domaine: DomaineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default domaineRoute;

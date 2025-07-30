import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import OpportuniteResolve from './route/opportunite-routing-resolve.service';

const opportuniteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/opportunite.component').then(m => m.OpportuniteComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/opportunite-detail.component').then(m => m.OpportuniteDetailComponent),
    resolve: {
      opportunite: OpportuniteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/opportunite-update.component').then(m => m.OpportuniteUpdateComponent),
    resolve: {
      opportunite: OpportuniteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/opportunite-update.component').then(m => m.OpportuniteUpdateComponent),
    resolve: {
      opportunite: OpportuniteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default opportuniteRoute;

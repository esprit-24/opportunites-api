import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VilleResolve from './route/ville-routing-resolve.service';

const villeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ville.component').then(m => m.VilleComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ville-detail.component').then(m => m.VilleDetailComponent),
    resolve: {
      ville: VilleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ville-update.component').then(m => m.VilleUpdateComponent),
    resolve: {
      ville: VilleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ville-update.component').then(m => m.VilleUpdateComponent),
    resolve: {
      ville: VilleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default villeRoute;

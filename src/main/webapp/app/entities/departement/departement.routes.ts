import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DepartementResolve from './route/departement-routing-resolve.service';

const departementRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/departement.component').then(m => m.DepartementComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/departement-detail.component').then(m => m.DepartementDetailComponent),
    resolve: {
      departement: DepartementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/departement-update.component').then(m => m.DepartementUpdateComponent),
    resolve: {
      departement: DepartementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/departement-update.component').then(m => m.DepartementUpdateComponent),
    resolve: {
      departement: DepartementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default departementRoute;

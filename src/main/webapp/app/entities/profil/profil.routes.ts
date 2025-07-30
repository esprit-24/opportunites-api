import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProfilResolve from './route/profil-routing-resolve.service';

const profilRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/profil.component').then(m => m.ProfilComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/profil-detail.component').then(m => m.ProfilDetailComponent),
    resolve: {
      profil: ProfilResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/profil-update.component').then(m => m.ProfilUpdateComponent),
    resolve: {
      profil: ProfilResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/profil-update.component').then(m => m.ProfilUpdateComponent),
    resolve: {
      profil: ProfilResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default profilRoute;

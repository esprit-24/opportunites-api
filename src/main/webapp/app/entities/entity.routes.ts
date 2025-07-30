import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'opportunitesApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'profil',
    data: { pageTitle: 'opportunitesApp.profil.home.title' },
    loadChildren: () => import('./profil/profil.routes'),
  },
  {
    path: 'domaine',
    data: { pageTitle: 'opportunitesApp.domaine.home.title' },
    loadChildren: () => import('./domaine/domaine.routes'),
  },
  {
    path: 'region',
    data: { pageTitle: 'opportunitesApp.region.home.title' },
    loadChildren: () => import('./region/region.routes'),
  },
  {
    path: 'departement',
    data: { pageTitle: 'opportunitesApp.departement.home.title' },
    loadChildren: () => import('./departement/departement.routes'),
  },
  {
    path: 'ville',
    data: { pageTitle: 'opportunitesApp.ville.home.title' },
    loadChildren: () => import('./ville/ville.routes'),
  },
  {
    path: 'organisation',
    data: { pageTitle: 'opportunitesApp.organisation.home.title' },
    loadChildren: () => import('./organisation/organisation.routes'),
  },
  {
    path: 'candidat',
    data: { pageTitle: 'opportunitesApp.candidat.home.title' },
    loadChildren: () => import('./candidat/candidat.routes'),
  },
  {
    path: 'recruteur',
    data: { pageTitle: 'opportunitesApp.recruteur.home.title' },
    loadChildren: () => import('./recruteur/recruteur.routes'),
  },
  {
    path: 'opportunite',
    data: { pageTitle: 'opportunitesApp.opportunite.home.title' },
    loadChildren: () => import('./opportunite/opportunite.routes'),
  },
  {
    path: 'candidature',
    data: { pageTitle: 'opportunitesApp.candidature.home.title' },
    loadChildren: () => import('./candidature/candidature.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;

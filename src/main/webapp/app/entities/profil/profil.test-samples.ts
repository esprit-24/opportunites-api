import { IProfil, NewProfil } from './profil.model';

export const sampleWithRequiredData: IProfil = {
  id: 21388,
  intitule: 'psitt',
};

export const sampleWithPartialData: IProfil = {
  id: 16576,
  intitule: 'refroidir',
};

export const sampleWithFullData: IProfil = {
  id: 31533,
  intitule: 'psitt',
};

export const sampleWithNewData: NewProfil = {
  intitule: 'coin-coin coac coac',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

import { IDepartement, NewDepartement } from './departement.model';

export const sampleWithRequiredData: IDepartement = {
  id: 4097,
  nom: 'bè',
};

export const sampleWithPartialData: IDepartement = {
  id: 21679,
  nom: 'ding',
};

export const sampleWithFullData: IDepartement = {
  id: 19919,
  nom: 'cocorico',
};

export const sampleWithNewData: NewDepartement = {
  nom: 'près',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

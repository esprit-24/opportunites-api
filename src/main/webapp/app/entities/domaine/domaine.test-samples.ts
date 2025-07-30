import { IDomaine, NewDomaine } from './domaine.model';

export const sampleWithRequiredData: IDomaine = {
  id: 4899,
  intitule: 'hebdomadaire',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IDomaine = {
  id: 12693,
  intitule: 'collègue proche de',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IDomaine = {
  id: 3417,
  intitule: 'marcher',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewDomaine = {
  intitule: 'ensemble porte-parole',
  description: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

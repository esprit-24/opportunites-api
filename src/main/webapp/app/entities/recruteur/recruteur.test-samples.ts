import { IRecruteur, NewRecruteur } from './recruteur.model';

export const sampleWithRequiredData: IRecruteur = {
  id: 3365,
};

export const sampleWithPartialData: IRecruteur = {
  id: 26167,
};

export const sampleWithFullData: IRecruteur = {
  id: 13282,
  titreProfessionnel: 'partenaire pourvu que porte-parole',
  biographie: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewRecruteur = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

import { IVille, NewVille } from './ville.model';

export const sampleWithRequiredData: IVille = {
  id: 23408,
  nom: 'promettre membre à vie',
};

export const sampleWithPartialData: IVille = {
  id: 667,
  nom: 'moderne dessiner parce que',
};

export const sampleWithFullData: IVille = {
  id: 20161,
  nom: 'jadis splendide',
};

export const sampleWithNewData: NewVille = {
  nom: 'apprendre autrefois',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

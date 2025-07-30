import { IRegion, NewRegion } from './region.model';

export const sampleWithRequiredData: IRegion = {
  id: 20776,
  nom: "au-dessus de d'après résigner",
};

export const sampleWithPartialData: IRegion = {
  id: 16206,
  nom: 'horrible tracer dispenser',
};

export const sampleWithFullData: IRegion = {
  id: 32233,
  nom: 'a',
};

export const sampleWithNewData: NewRegion = {
  nom: 'en plus de marcher badaboum',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

import dayjs from 'dayjs/esm';

import { ICandidat, NewCandidat } from './candidat.model';

export const sampleWithRequiredData: ICandidat = {
  id: 9734,
  niveauEtude: 'LICENCE',
};

export const sampleWithPartialData: ICandidat = {
  id: 9060,
  dateNaissance: dayjs('2025-07-30T08:48'),
  niveauEtude: 'BAC',
  cvUrl: 'partenaire parfois miam',
  statutActuel: 'super police',
};

export const sampleWithFullData: ICandidat = {
  id: 4632,
  dateNaissance: dayjs('2025-07-29T19:43'),
  niveauEtude: 'MASTER',
  cvUrl: 'recta prout hôte',
  statutActuel: 'exercer',
};

export const sampleWithNewData: NewCandidat = {
  niveauEtude: 'BFEM',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

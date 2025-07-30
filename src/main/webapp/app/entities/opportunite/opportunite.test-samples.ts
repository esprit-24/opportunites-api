import dayjs from 'dayjs/esm';

import { IOpportunite, NewOpportunite } from './opportunite.model';

export const sampleWithRequiredData: IOpportunite = {
  id: 6986,
  titre: 'coin-coin absolument areu areu',
  description: '../fake-data/blob/hipster.txt',
  dateDebut: dayjs('2025-07-29T11:10'),
  nombrePostes: 29613,
};

export const sampleWithPartialData: IOpportunite = {
  id: 14447,
  titre: 'insipide bof',
  description: '../fake-data/blob/hipster.txt',
  dateDebut: dayjs('2025-07-29T14:27'),
  nombrePostes: 8759,
  salaire: 22278.86,
  statut: 'ACTIVE',
  typeContrat: 'CDI',
};

export const sampleWithFullData: IOpportunite = {
  id: 3518,
  titre: 'vouh perplexe',
  description: '../fake-data/blob/hipster.txt',
  dateDebut: dayjs('2025-07-30T07:06'),
  dateFin: dayjs('2025-07-30T01:52'),
  adresse: 'pendant drelin au-dessus',
  niveauEtudeRequis: 'DOCTORAT',
  nombrePostes: 25137,
  salaire: 25956.26,
  statut: 'EXPIREE',
  typeContrat: 'STAGE',
};

export const sampleWithNewData: NewOpportunite = {
  titre: 'ah',
  description: '../fake-data/blob/hipster.txt',
  dateDebut: dayjs('2025-07-30T05:16'),
  nombrePostes: 16162,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

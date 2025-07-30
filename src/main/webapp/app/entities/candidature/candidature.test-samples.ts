import dayjs from 'dayjs/esm';

import { ICandidature, NewCandidature } from './candidature.model';

export const sampleWithRequiredData: ICandidature = {
  id: 28558,
  datePostulation: dayjs('2025-07-29T20:59'),
};

export const sampleWithPartialData: ICandidature = {
  id: 13073,
  datePostulation: dayjs('2025-07-29T17:21'),
  statutCandidature: 'VUE_RECRUTEUR',
};

export const sampleWithFullData: ICandidature = {
  id: 23153,
  datePostulation: dayjs('2025-07-30T10:51'),
  statutCandidature: 'ACCEPTEE_RECRUTEUR',
  lettreMotivation: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewCandidature = {
  datePostulation: dayjs('2025-07-29T14:57'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

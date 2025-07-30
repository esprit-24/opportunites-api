import dayjs from 'dayjs/esm';
import { IOpportunite } from 'app/entities/opportunite/opportunite.model';
import { ICandidat } from 'app/entities/candidat/candidat.model';
import { StatutCandidature } from 'app/entities/enumerations/statut-candidature.model';

export interface ICandidature {
  id: number;
  datePostulation?: dayjs.Dayjs | null;
  statutCandidature?: keyof typeof StatutCandidature | null;
  lettreMotivation?: string | null;
  opportunite?: Pick<IOpportunite, 'id'> | null;
  candidat?: Pick<ICandidat, 'id'> | null;
}

export type NewCandidature = Omit<ICandidature, 'id'> & { id: null };

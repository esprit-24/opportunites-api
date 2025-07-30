import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IProfil } from 'app/entities/profil/profil.model';
import { IDomaine } from 'app/entities/domaine/domaine.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';

export interface ICandidat {
  id: number;
  dateNaissance?: dayjs.Dayjs | null;
  niveauEtude?: keyof typeof NiveauEtude | null;
  cvUrl?: string | null;
  statutActuel?: string | null;
  user?: Pick<IUser, 'id'> | null;
  profil?: Pick<IProfil, 'id'> | null;
  domaine?: Pick<IDomaine, 'id'> | null;
}

export type NewCandidat = Omit<ICandidat, 'id'> & { id: null };

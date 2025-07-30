import dayjs from 'dayjs/esm';
import { IDomaine } from 'app/entities/domaine/domaine.model';
import { IOrganisation } from 'app/entities/organisation/organisation.model';
import { IVille } from 'app/entities/ville/ville.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';
import { Statut } from 'app/entities/enumerations/statut.model';
import { TypeContrat } from 'app/entities/enumerations/type-contrat.model';

export interface IOpportunite {
  id: number;
  titre?: string | null;
  description?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  adresse?: string | null;
  niveauEtudeRequis?: keyof typeof NiveauEtude | null;
  nombrePostes?: number | null;
  salaire?: number | null;
  statut?: keyof typeof Statut | null;
  typeContrat?: keyof typeof TypeContrat | null;
  domaine?: Pick<IDomaine, 'id'> | null;
  organisation?: Pick<IOrganisation, 'id'> | null;
  ville?: Pick<IVille, 'id'> | null;
}

export type NewOpportunite = Omit<IOpportunite, 'id'> & { id: null };

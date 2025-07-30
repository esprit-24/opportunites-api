import { IVille } from 'app/entities/ville/ville.model';

export interface IOrganisation {
  id: number;
  nom?: string | null;
  presentation?: string | null;
  secteurActivite?: string | null;
  logoUrl?: string | null;
  adresse?: string | null;
  siteWeb?: string | null;
  emailContact?: string | null;
  telephone?: string | null;
  ville?: Pick<IVille, 'id'> | null;
}

export type NewOrganisation = Omit<IOrganisation, 'id'> & { id: null };

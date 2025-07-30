import { IUser } from 'app/entities/user/user.model';
import { IOrganisation } from 'app/entities/organisation/organisation.model';

export interface IRecruteur {
  id: number;
  titreProfessionnel?: string | null;
  biographie?: string | null;
  user?: Pick<IUser, 'id'> | null;
  organisation?: Pick<IOrganisation, 'id'> | null;
}

export type NewRecruteur = Omit<IRecruteur, 'id'> & { id: null };

import { IDepartement } from 'app/entities/departement/departement.model';

export interface IVille {
  id: number;
  nom?: string | null;
  departement?: Pick<IDepartement, 'id'> | null;
}

export type NewVille = Omit<IVille, 'id'> & { id: null };

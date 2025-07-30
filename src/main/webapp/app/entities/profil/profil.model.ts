import { IDomaine } from 'app/entities/domaine/domaine.model';

export interface IProfil {
  id: number;
  intitule?: string | null;
  domaine?: Pick<IDomaine, 'id'> | null;
}

export type NewProfil = Omit<IProfil, 'id'> & { id: null };

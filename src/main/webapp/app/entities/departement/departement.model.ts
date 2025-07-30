import { IRegion } from 'app/entities/region/region.model';

export interface IDepartement {
  id: number;
  nom?: string | null;
  region?: Pick<IRegion, 'id'> | null;
}

export type NewDepartement = Omit<IDepartement, 'id'> & { id: null };

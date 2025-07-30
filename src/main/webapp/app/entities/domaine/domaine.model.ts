export interface IDomaine {
  id: number;
  intitule?: string | null;
  description?: string | null;
}

export type NewDomaine = Omit<IDomaine, 'id'> & { id: null };

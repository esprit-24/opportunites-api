import { IOrganisation, NewOrganisation } from './organisation.model';

export const sampleWithRequiredData: IOrganisation = {
  id: 15652,
  nom: 'puisque au-dessus de',
  presentation: '../fake-data/blob/hipster.txt',
  adresse: 'badaboum',
  emailContact: 'adorable plouf révéler',
};

export const sampleWithPartialData: IOrganisation = {
  id: 4672,
  nom: 'enfin maintenant à peine',
  presentation: '../fake-data/blob/hipster.txt',
  logoUrl: 'énergique',
  adresse: 'envers gravir surprendre',
  siteWeb: 'foule jusqu’à ce que',
  emailContact: 'nonobstant',
};

export const sampleWithFullData: IOrganisation = {
  id: 3664,
  nom: 'camarade',
  presentation: '../fake-data/blob/hipster.txt',
  secteurActivite: 'puisque',
  logoUrl: 'décourager secours',
  adresse: 'supprimer',
  siteWeb: 'fort que',
  emailContact: 'fonctionnaire apte à travers',
  telephone: '+33 167973029',
};

export const sampleWithNewData: NewOrganisation = {
  nom: 'hebdomadaire',
  presentation: '../fake-data/blob/hipster.txt',
  adresse: 'bzzz membre du personnel',
  emailContact: 'souple avant',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

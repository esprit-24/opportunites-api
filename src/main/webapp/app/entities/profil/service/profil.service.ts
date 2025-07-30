import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProfil, NewProfil } from '../profil.model';

export type PartialUpdateProfil = Partial<IProfil> & Pick<IProfil, 'id'>;

export type EntityResponseType = HttpResponse<IProfil>;
export type EntityArrayResponseType = HttpResponse<IProfil[]>;

@Injectable({ providedIn: 'root' })
export class ProfilService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/profils');

  create(profil: NewProfil): Observable<EntityResponseType> {
    return this.http.post<IProfil>(this.resourceUrl, profil, { observe: 'response' });
  }

  update(profil: IProfil): Observable<EntityResponseType> {
    return this.http.put<IProfil>(`${this.resourceUrl}/${this.getProfilIdentifier(profil)}`, profil, { observe: 'response' });
  }

  partialUpdate(profil: PartialUpdateProfil): Observable<EntityResponseType> {
    return this.http.patch<IProfil>(`${this.resourceUrl}/${this.getProfilIdentifier(profil)}`, profil, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProfil>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProfil[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProfilIdentifier(profil: Pick<IProfil, 'id'>): number {
    return profil.id;
  }

  compareProfil(o1: Pick<IProfil, 'id'> | null, o2: Pick<IProfil, 'id'> | null): boolean {
    return o1 && o2 ? this.getProfilIdentifier(o1) === this.getProfilIdentifier(o2) : o1 === o2;
  }

  addProfilToCollectionIfMissing<Type extends Pick<IProfil, 'id'>>(
    profilCollection: Type[],
    ...profilsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const profils: Type[] = profilsToCheck.filter(isPresent);
    if (profils.length > 0) {
      const profilCollectionIdentifiers = profilCollection.map(profilItem => this.getProfilIdentifier(profilItem));
      const profilsToAdd = profils.filter(profilItem => {
        const profilIdentifier = this.getProfilIdentifier(profilItem);
        if (profilCollectionIdentifiers.includes(profilIdentifier)) {
          return false;
        }
        profilCollectionIdentifiers.push(profilIdentifier);
        return true;
      });
      return [...profilsToAdd, ...profilCollection];
    }
    return profilCollection;
  }
}

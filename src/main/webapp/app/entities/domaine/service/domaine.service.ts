import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDomaine, NewDomaine } from '../domaine.model';

export type PartialUpdateDomaine = Partial<IDomaine> & Pick<IDomaine, 'id'>;

export type EntityResponseType = HttpResponse<IDomaine>;
export type EntityArrayResponseType = HttpResponse<IDomaine[]>;

@Injectable({ providedIn: 'root' })
export class DomaineService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/domaines');

  create(domaine: NewDomaine): Observable<EntityResponseType> {
    return this.http.post<IDomaine>(this.resourceUrl, domaine, { observe: 'response' });
  }

  update(domaine: IDomaine): Observable<EntityResponseType> {
    return this.http.put<IDomaine>(`${this.resourceUrl}/${this.getDomaineIdentifier(domaine)}`, domaine, { observe: 'response' });
  }

  partialUpdate(domaine: PartialUpdateDomaine): Observable<EntityResponseType> {
    return this.http.patch<IDomaine>(`${this.resourceUrl}/${this.getDomaineIdentifier(domaine)}`, domaine, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDomaine>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDomaine[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDomaineIdentifier(domaine: Pick<IDomaine, 'id'>): number {
    return domaine.id;
  }

  compareDomaine(o1: Pick<IDomaine, 'id'> | null, o2: Pick<IDomaine, 'id'> | null): boolean {
    return o1 && o2 ? this.getDomaineIdentifier(o1) === this.getDomaineIdentifier(o2) : o1 === o2;
  }

  addDomaineToCollectionIfMissing<Type extends Pick<IDomaine, 'id'>>(
    domaineCollection: Type[],
    ...domainesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const domaines: Type[] = domainesToCheck.filter(isPresent);
    if (domaines.length > 0) {
      const domaineCollectionIdentifiers = domaineCollection.map(domaineItem => this.getDomaineIdentifier(domaineItem));
      const domainesToAdd = domaines.filter(domaineItem => {
        const domaineIdentifier = this.getDomaineIdentifier(domaineItem);
        if (domaineCollectionIdentifiers.includes(domaineIdentifier)) {
          return false;
        }
        domaineCollectionIdentifiers.push(domaineIdentifier);
        return true;
      });
      return [...domainesToAdd, ...domaineCollection];
    }
    return domaineCollection;
  }
}

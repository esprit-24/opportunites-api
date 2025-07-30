import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOpportunite, NewOpportunite } from '../opportunite.model';

export type PartialUpdateOpportunite = Partial<IOpportunite> & Pick<IOpportunite, 'id'>;

type RestOf<T extends IOpportunite | NewOpportunite> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestOpportunite = RestOf<IOpportunite>;

export type NewRestOpportunite = RestOf<NewOpportunite>;

export type PartialUpdateRestOpportunite = RestOf<PartialUpdateOpportunite>;

export type EntityResponseType = HttpResponse<IOpportunite>;
export type EntityArrayResponseType = HttpResponse<IOpportunite[]>;

@Injectable({ providedIn: 'root' })
export class OpportuniteService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/opportunites');

  create(opportunite: NewOpportunite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(opportunite);
    return this.http
      .post<RestOpportunite>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(opportunite: IOpportunite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(opportunite);
    return this.http
      .put<RestOpportunite>(`${this.resourceUrl}/${this.getOpportuniteIdentifier(opportunite)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(opportunite: PartialUpdateOpportunite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(opportunite);
    return this.http
      .patch<RestOpportunite>(`${this.resourceUrl}/${this.getOpportuniteIdentifier(opportunite)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOpportunite>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOpportunite[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOpportuniteIdentifier(opportunite: Pick<IOpportunite, 'id'>): number {
    return opportunite.id;
  }

  compareOpportunite(o1: Pick<IOpportunite, 'id'> | null, o2: Pick<IOpportunite, 'id'> | null): boolean {
    return o1 && o2 ? this.getOpportuniteIdentifier(o1) === this.getOpportuniteIdentifier(o2) : o1 === o2;
  }

  addOpportuniteToCollectionIfMissing<Type extends Pick<IOpportunite, 'id'>>(
    opportuniteCollection: Type[],
    ...opportunitesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const opportunites: Type[] = opportunitesToCheck.filter(isPresent);
    if (opportunites.length > 0) {
      const opportuniteCollectionIdentifiers = opportuniteCollection.map(opportuniteItem => this.getOpportuniteIdentifier(opportuniteItem));
      const opportunitesToAdd = opportunites.filter(opportuniteItem => {
        const opportuniteIdentifier = this.getOpportuniteIdentifier(opportuniteItem);
        if (opportuniteCollectionIdentifiers.includes(opportuniteIdentifier)) {
          return false;
        }
        opportuniteCollectionIdentifiers.push(opportuniteIdentifier);
        return true;
      });
      return [...opportunitesToAdd, ...opportuniteCollection];
    }
    return opportuniteCollection;
  }

  protected convertDateFromClient<T extends IOpportunite | NewOpportunite | PartialUpdateOpportunite>(opportunite: T): RestOf<T> {
    return {
      ...opportunite,
      dateDebut: opportunite.dateDebut?.toJSON() ?? null,
      dateFin: opportunite.dateFin?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restOpportunite: RestOpportunite): IOpportunite {
    return {
      ...restOpportunite,
      dateDebut: restOpportunite.dateDebut ? dayjs(restOpportunite.dateDebut) : undefined,
      dateFin: restOpportunite.dateFin ? dayjs(restOpportunite.dateFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOpportunite>): HttpResponse<IOpportunite> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOpportunite[]>): HttpResponse<IOpportunite[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

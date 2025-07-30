import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICandidat, NewCandidat } from '../candidat.model';

export type PartialUpdateCandidat = Partial<ICandidat> & Pick<ICandidat, 'id'>;

type RestOf<T extends ICandidat | NewCandidat> = Omit<T, 'dateNaissance'> & {
  dateNaissance?: string | null;
};

export type RestCandidat = RestOf<ICandidat>;

export type NewRestCandidat = RestOf<NewCandidat>;

export type PartialUpdateRestCandidat = RestOf<PartialUpdateCandidat>;

export type EntityResponseType = HttpResponse<ICandidat>;
export type EntityArrayResponseType = HttpResponse<ICandidat[]>;

@Injectable({ providedIn: 'root' })
export class CandidatService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/candidats');

  create(candidat: NewCandidat): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidat);
    return this.http
      .post<RestCandidat>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(candidat: ICandidat): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidat);
    return this.http
      .put<RestCandidat>(`${this.resourceUrl}/${this.getCandidatIdentifier(candidat)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(candidat: PartialUpdateCandidat): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidat);
    return this.http
      .patch<RestCandidat>(`${this.resourceUrl}/${this.getCandidatIdentifier(candidat)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCandidat>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCandidat[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCandidatIdentifier(candidat: Pick<ICandidat, 'id'>): number {
    return candidat.id;
  }

  compareCandidat(o1: Pick<ICandidat, 'id'> | null, o2: Pick<ICandidat, 'id'> | null): boolean {
    return o1 && o2 ? this.getCandidatIdentifier(o1) === this.getCandidatIdentifier(o2) : o1 === o2;
  }

  addCandidatToCollectionIfMissing<Type extends Pick<ICandidat, 'id'>>(
    candidatCollection: Type[],
    ...candidatsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const candidats: Type[] = candidatsToCheck.filter(isPresent);
    if (candidats.length > 0) {
      const candidatCollectionIdentifiers = candidatCollection.map(candidatItem => this.getCandidatIdentifier(candidatItem));
      const candidatsToAdd = candidats.filter(candidatItem => {
        const candidatIdentifier = this.getCandidatIdentifier(candidatItem);
        if (candidatCollectionIdentifiers.includes(candidatIdentifier)) {
          return false;
        }
        candidatCollectionIdentifiers.push(candidatIdentifier);
        return true;
      });
      return [...candidatsToAdd, ...candidatCollection];
    }
    return candidatCollection;
  }

  protected convertDateFromClient<T extends ICandidat | NewCandidat | PartialUpdateCandidat>(candidat: T): RestOf<T> {
    return {
      ...candidat,
      dateNaissance: candidat.dateNaissance?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCandidat: RestCandidat): ICandidat {
    return {
      ...restCandidat,
      dateNaissance: restCandidat.dateNaissance ? dayjs(restCandidat.dateNaissance) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCandidat>): HttpResponse<ICandidat> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCandidat[]>): HttpResponse<ICandidat[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

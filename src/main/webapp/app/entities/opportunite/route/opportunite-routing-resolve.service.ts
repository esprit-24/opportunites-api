import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOpportunite } from '../opportunite.model';
import { OpportuniteService } from '../service/opportunite.service';

const opportuniteResolve = (route: ActivatedRouteSnapshot): Observable<null | IOpportunite> => {
  const id = route.params.id;
  if (id) {
    return inject(OpportuniteService)
      .find(id)
      .pipe(
        mergeMap((opportunite: HttpResponse<IOpportunite>) => {
          if (opportunite.body) {
            return of(opportunite.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default opportuniteResolve;

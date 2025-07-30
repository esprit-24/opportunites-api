import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVille } from '../ville.model';
import { VilleService } from '../service/ville.service';

const villeResolve = (route: ActivatedRouteSnapshot): Observable<null | IVille> => {
  const id = route.params.id;
  if (id) {
    return inject(VilleService)
      .find(id)
      .pipe(
        mergeMap((ville: HttpResponse<IVille>) => {
          if (ville.body) {
            return of(ville.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default villeResolve;

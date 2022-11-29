import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IUnitOfMeasure, UnitOfMeasure } from 'app/shared/model/unit-of-measure.model';
import { UnitOfMeasureService } from './unit-of-measure.service';
import { UnitOfMeasureComponent } from './unit-of-measure.component';
import { UnitOfMeasureDetailComponent } from './unit-of-measure-detail.component';
import { UnitOfMeasureUpdateComponent } from './unit-of-measure-update.component';

@Injectable({ providedIn: 'root' })
export class UnitOfMeasureResolve implements Resolve<IUnitOfMeasure> {
  constructor(private service: UnitOfMeasureService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUnitOfMeasure> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((unitOfMeasure: HttpResponse<UnitOfMeasure>) => {
          if (unitOfMeasure.body) {
            return of(unitOfMeasure.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UnitOfMeasure());
  }
}

export const unitOfMeasureRoute: Routes = [
  {
    path: '',
    component: UnitOfMeasureComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UnitOfMeasures'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: UnitOfMeasureDetailComponent,
    resolve: {
      unitOfMeasure: UnitOfMeasureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UnitOfMeasures'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: UnitOfMeasureUpdateComponent,
    resolve: {
      unitOfMeasure: UnitOfMeasureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UnitOfMeasures'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: UnitOfMeasureUpdateComponent,
    resolve: {
      unitOfMeasure: UnitOfMeasureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UnitOfMeasures'
    },
    canActivate: [UserRouteAccessService]
  }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILicenseType, LicenseType } from 'app/shared/model/license-type.model';
import { LicenseTypeService } from './license-type.service';
import { LicenseTypeComponent } from './license-type.component';
import { LicenseTypeDetailComponent } from './license-type-detail.component';
import { LicenseTypeUpdateComponent } from './license-type-update.component';

@Injectable({ providedIn: 'root' })
export class LicenseTypeResolve implements Resolve<ILicenseType> {
  constructor(private service: LicenseTypeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILicenseType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((licenseType: HttpResponse<LicenseType>) => {
          if (licenseType.body) {
            return of(licenseType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LicenseType());
  }
}

export const licenseTypeRoute: Routes = [
  {
    path: '',
    component: LicenseTypeComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'LicenseTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: LicenseTypeDetailComponent,
    resolve: {
      licenseType: LicenseTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'LicenseTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: LicenseTypeUpdateComponent,
    resolve: {
      licenseType: LicenseTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'LicenseTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: LicenseTypeUpdateComponent,
    resolve: {
      licenseType: LicenseTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'LicenseTypes'
    },
    canActivate: [UserRouteAccessService]
  }
];

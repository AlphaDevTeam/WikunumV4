import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEmployeeType, EmployeeType } from 'app/shared/model/employee-type.model';
import { EmployeeTypeService } from './employee-type.service';
import { EmployeeTypeComponent } from './employee-type.component';
import { EmployeeTypeDetailComponent } from './employee-type-detail.component';
import { EmployeeTypeUpdateComponent } from './employee-type-update.component';

@Injectable({ providedIn: 'root' })
export class EmployeeTypeResolve implements Resolve<IEmployeeType> {
  constructor(private service: EmployeeTypeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((employeeType: HttpResponse<EmployeeType>) => {
          if (employeeType.body) {
            return of(employeeType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EmployeeType());
  }
}

export const employeeTypeRoute: Routes = [
  {
    path: '',
    component: EmployeeTypeComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'EmployeeTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: EmployeeTypeDetailComponent,
    resolve: {
      employeeType: EmployeeTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EmployeeTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: EmployeeTypeUpdateComponent,
    resolve: {
      employeeType: EmployeeTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EmployeeTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: EmployeeTypeUpdateComponent,
    resolve: {
      employeeType: EmployeeTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EmployeeTypes'
    },
    canActivate: [UserRouteAccessService]
  }
];

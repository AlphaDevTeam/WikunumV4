import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEmployeeAccountBalance, EmployeeAccountBalance } from 'app/shared/model/employee-account-balance.model';
import { EmployeeAccountBalanceService } from './employee-account-balance.service';
import { EmployeeAccountBalanceComponent } from './employee-account-balance.component';
import { EmployeeAccountBalanceDetailComponent } from './employee-account-balance-detail.component';
import { EmployeeAccountBalanceUpdateComponent } from './employee-account-balance-update.component';

@Injectable({ providedIn: 'root' })
export class EmployeeAccountBalanceResolve implements Resolve<IEmployeeAccountBalance> {
  constructor(private service: EmployeeAccountBalanceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeAccountBalance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((employeeAccountBalance: HttpResponse<EmployeeAccountBalance>) => {
          if (employeeAccountBalance.body) {
            return of(employeeAccountBalance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EmployeeAccountBalance());
  }
}

export const employeeAccountBalanceRoute: Routes = [
  {
    path: '',
    component: EmployeeAccountBalanceComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'EmployeeAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: EmployeeAccountBalanceDetailComponent,
    resolve: {
      employeeAccountBalance: EmployeeAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EmployeeAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: EmployeeAccountBalanceUpdateComponent,
    resolve: {
      employeeAccountBalance: EmployeeAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EmployeeAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: EmployeeAccountBalanceUpdateComponent,
    resolve: {
      employeeAccountBalance: EmployeeAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EmployeeAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  }
];

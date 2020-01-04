import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICustomerAccountBalance, CustomerAccountBalance } from 'app/shared/model/customer-account-balance.model';
import { CustomerAccountBalanceService } from './customer-account-balance.service';
import { CustomerAccountBalanceComponent } from './customer-account-balance.component';
import { CustomerAccountBalanceDetailComponent } from './customer-account-balance-detail.component';
import { CustomerAccountBalanceUpdateComponent } from './customer-account-balance-update.component';

@Injectable({ providedIn: 'root' })
export class CustomerAccountBalanceResolve implements Resolve<ICustomerAccountBalance> {
  constructor(private service: CustomerAccountBalanceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICustomerAccountBalance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((customerAccountBalance: HttpResponse<CustomerAccountBalance>) => {
          if (customerAccountBalance.body) {
            return of(customerAccountBalance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CustomerAccountBalance());
  }
}

export const customerAccountBalanceRoute: Routes = [
  {
    path: '',
    component: CustomerAccountBalanceComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'CustomerAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CustomerAccountBalanceDetailComponent,
    resolve: {
      customerAccountBalance: CustomerAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CustomerAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CustomerAccountBalanceUpdateComponent,
    resolve: {
      customerAccountBalance: CustomerAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CustomerAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CustomerAccountBalanceUpdateComponent,
    resolve: {
      customerAccountBalance: CustomerAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CustomerAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICustomerAccount, CustomerAccount } from 'app/shared/model/customer-account.model';
import { CustomerAccountService } from './customer-account.service';
import { CustomerAccountComponent } from './customer-account.component';
import { CustomerAccountDetailComponent } from './customer-account-detail.component';
import { CustomerAccountUpdateComponent } from './customer-account-update.component';

@Injectable({ providedIn: 'root' })
export class CustomerAccountResolve implements Resolve<ICustomerAccount> {
  constructor(private service: CustomerAccountService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICustomerAccount> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((customerAccount: HttpResponse<CustomerAccount>) => {
          if (customerAccount.body) {
            return of(customerAccount.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CustomerAccount());
  }
}

export const customerAccountRoute: Routes = [
  {
    path: '',
    component: CustomerAccountComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CustomerAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CustomerAccountDetailComponent,
    resolve: {
      customerAccount: CustomerAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CustomerAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CustomerAccountUpdateComponent,
    resolve: {
      customerAccount: CustomerAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CustomerAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CustomerAccountUpdateComponent,
    resolve: {
      customerAccount: CustomerAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CustomerAccounts'
    },
    canActivate: [UserRouteAccessService]
  }
];

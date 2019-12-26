import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICashPaymentVoucherCustomer, CashPaymentVoucherCustomer } from 'app/shared/model/cash-payment-voucher-customer.model';
import { CashPaymentVoucherCustomerService } from './cash-payment-voucher-customer.service';
import { CashPaymentVoucherCustomerComponent } from './cash-payment-voucher-customer.component';
import { CashPaymentVoucherCustomerDetailComponent } from './cash-payment-voucher-customer-detail.component';
import { CashPaymentVoucherCustomerUpdateComponent } from './cash-payment-voucher-customer-update.component';

@Injectable({ providedIn: 'root' })
export class CashPaymentVoucherCustomerResolve implements Resolve<ICashPaymentVoucherCustomer> {
  constructor(private service: CashPaymentVoucherCustomerService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICashPaymentVoucherCustomer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((cashPaymentVoucherCustomer: HttpResponse<CashPaymentVoucherCustomer>) => {
          if (cashPaymentVoucherCustomer.body) {
            return of(cashPaymentVoucherCustomer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CashPaymentVoucherCustomer());
  }
}

export const cashPaymentVoucherCustomerRoute: Routes = [
  {
    path: '',
    component: CashPaymentVoucherCustomerComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashPaymentVoucherCustomers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CashPaymentVoucherCustomerDetailComponent,
    resolve: {
      cashPaymentVoucherCustomer: CashPaymentVoucherCustomerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashPaymentVoucherCustomers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CashPaymentVoucherCustomerUpdateComponent,
    resolve: {
      cashPaymentVoucherCustomer: CashPaymentVoucherCustomerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashPaymentVoucherCustomers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CashPaymentVoucherCustomerUpdateComponent,
    resolve: {
      cashPaymentVoucherCustomer: CashPaymentVoucherCustomerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashPaymentVoucherCustomers'
    },
    canActivate: [UserRouteAccessService]
  }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICashReceiptVoucherCustomer, CashReceiptVoucherCustomer } from 'app/shared/model/cash-receipt-voucher-customer.model';
import { CashReceiptVoucherCustomerService } from './cash-receipt-voucher-customer.service';
import { CashReceiptVoucherCustomerComponent } from './cash-receipt-voucher-customer.component';
import { CashReceiptVoucherCustomerDetailComponent } from './cash-receipt-voucher-customer-detail.component';
import { CashReceiptVoucherCustomerUpdateComponent } from './cash-receipt-voucher-customer-update.component';

@Injectable({ providedIn: 'root' })
export class CashReceiptVoucherCustomerResolve implements Resolve<ICashReceiptVoucherCustomer> {
  constructor(private service: CashReceiptVoucherCustomerService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICashReceiptVoucherCustomer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((cashReceiptVoucherCustomer: HttpResponse<CashReceiptVoucherCustomer>) => {
          if (cashReceiptVoucherCustomer.body) {
            return of(cashReceiptVoucherCustomer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CashReceiptVoucherCustomer());
  }
}

export const cashReceiptVoucherCustomerRoute: Routes = [
  {
    path: '',
    component: CashReceiptVoucherCustomerComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashReceiptVoucherCustomers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CashReceiptVoucherCustomerDetailComponent,
    resolve: {
      cashReceiptVoucherCustomer: CashReceiptVoucherCustomerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashReceiptVoucherCustomers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CashReceiptVoucherCustomerUpdateComponent,
    resolve: {
      cashReceiptVoucherCustomer: CashReceiptVoucherCustomerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashReceiptVoucherCustomers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CashReceiptVoucherCustomerUpdateComponent,
    resolve: {
      cashReceiptVoucherCustomer: CashReceiptVoucherCustomerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashReceiptVoucherCustomers'
    },
    canActivate: [UserRouteAccessService]
  }
];

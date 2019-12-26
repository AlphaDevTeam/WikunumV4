import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICashPaymentVoucherExpense, CashPaymentVoucherExpense } from 'app/shared/model/cash-payment-voucher-expense.model';
import { CashPaymentVoucherExpenseService } from './cash-payment-voucher-expense.service';
import { CashPaymentVoucherExpenseComponent } from './cash-payment-voucher-expense.component';
import { CashPaymentVoucherExpenseDetailComponent } from './cash-payment-voucher-expense-detail.component';
import { CashPaymentVoucherExpenseUpdateComponent } from './cash-payment-voucher-expense-update.component';

@Injectable({ providedIn: 'root' })
export class CashPaymentVoucherExpenseResolve implements Resolve<ICashPaymentVoucherExpense> {
  constructor(private service: CashPaymentVoucherExpenseService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICashPaymentVoucherExpense> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((cashPaymentVoucherExpense: HttpResponse<CashPaymentVoucherExpense>) => {
          if (cashPaymentVoucherExpense.body) {
            return of(cashPaymentVoucherExpense.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CashPaymentVoucherExpense());
  }
}

export const cashPaymentVoucherExpenseRoute: Routes = [
  {
    path: '',
    component: CashPaymentVoucherExpenseComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashPaymentVoucherExpenses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CashPaymentVoucherExpenseDetailComponent,
    resolve: {
      cashPaymentVoucherExpense: CashPaymentVoucherExpenseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashPaymentVoucherExpenses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CashPaymentVoucherExpenseUpdateComponent,
    resolve: {
      cashPaymentVoucherExpense: CashPaymentVoucherExpenseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashPaymentVoucherExpenses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CashPaymentVoucherExpenseUpdateComponent,
    resolve: {
      cashPaymentVoucherExpense: CashPaymentVoucherExpenseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashPaymentVoucherExpenses'
    },
    canActivate: [UserRouteAccessService]
  }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICashReceiptVoucherExpense, CashReceiptVoucherExpense } from 'app/shared/model/cash-receipt-voucher-expense.model';
import { CashReceiptVoucherExpenseService } from './cash-receipt-voucher-expense.service';
import { CashReceiptVoucherExpenseComponent } from './cash-receipt-voucher-expense.component';
import { CashReceiptVoucherExpenseDetailComponent } from './cash-receipt-voucher-expense-detail.component';
import { CashReceiptVoucherExpenseUpdateComponent } from './cash-receipt-voucher-expense-update.component';

@Injectable({ providedIn: 'root' })
export class CashReceiptVoucherExpenseResolve implements Resolve<ICashReceiptVoucherExpense> {
  constructor(private service: CashReceiptVoucherExpenseService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICashReceiptVoucherExpense> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((cashReceiptVoucherExpense: HttpResponse<CashReceiptVoucherExpense>) => {
          if (cashReceiptVoucherExpense.body) {
            return of(cashReceiptVoucherExpense.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CashReceiptVoucherExpense());
  }
}

export const cashReceiptVoucherExpenseRoute: Routes = [
  {
    path: '',
    component: CashReceiptVoucherExpenseComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'CashReceiptVoucherExpenses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CashReceiptVoucherExpenseDetailComponent,
    resolve: {
      cashReceiptVoucherExpense: CashReceiptVoucherExpenseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashReceiptVoucherExpenses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CashReceiptVoucherExpenseUpdateComponent,
    resolve: {
      cashReceiptVoucherExpense: CashReceiptVoucherExpenseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashReceiptVoucherExpenses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CashReceiptVoucherExpenseUpdateComponent,
    resolve: {
      cashReceiptVoucherExpense: CashReceiptVoucherExpenseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashReceiptVoucherExpenses'
    },
    canActivate: [UserRouteAccessService]
  }
];

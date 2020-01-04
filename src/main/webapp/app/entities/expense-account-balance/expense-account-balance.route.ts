import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IExpenseAccountBalance, ExpenseAccountBalance } from 'app/shared/model/expense-account-balance.model';
import { ExpenseAccountBalanceService } from './expense-account-balance.service';
import { ExpenseAccountBalanceComponent } from './expense-account-balance.component';
import { ExpenseAccountBalanceDetailComponent } from './expense-account-balance-detail.component';
import { ExpenseAccountBalanceUpdateComponent } from './expense-account-balance-update.component';

@Injectable({ providedIn: 'root' })
export class ExpenseAccountBalanceResolve implements Resolve<IExpenseAccountBalance> {
  constructor(private service: ExpenseAccountBalanceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExpenseAccountBalance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((expenseAccountBalance: HttpResponse<ExpenseAccountBalance>) => {
          if (expenseAccountBalance.body) {
            return of(expenseAccountBalance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExpenseAccountBalance());
  }
}

export const expenseAccountBalanceRoute: Routes = [
  {
    path: '',
    component: ExpenseAccountBalanceComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'ExpenseAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ExpenseAccountBalanceDetailComponent,
    resolve: {
      expenseAccountBalance: ExpenseAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExpenseAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ExpenseAccountBalanceUpdateComponent,
    resolve: {
      expenseAccountBalance: ExpenseAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExpenseAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ExpenseAccountBalanceUpdateComponent,
    resolve: {
      expenseAccountBalance: ExpenseAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExpenseAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IExpenseAccount, ExpenseAccount } from 'app/shared/model/expense-account.model';
import { ExpenseAccountService } from './expense-account.service';
import { ExpenseAccountComponent } from './expense-account.component';
import { ExpenseAccountDetailComponent } from './expense-account-detail.component';
import { ExpenseAccountUpdateComponent } from './expense-account-update.component';

@Injectable({ providedIn: 'root' })
export class ExpenseAccountResolve implements Resolve<IExpenseAccount> {
  constructor(private service: ExpenseAccountService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExpenseAccount> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((expenseAccount: HttpResponse<ExpenseAccount>) => {
          if (expenseAccount.body) {
            return of(expenseAccount.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExpenseAccount());
  }
}

export const expenseAccountRoute: Routes = [
  {
    path: '',
    component: ExpenseAccountComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'ExpenseAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ExpenseAccountDetailComponent,
    resolve: {
      expenseAccount: ExpenseAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExpenseAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ExpenseAccountUpdateComponent,
    resolve: {
      expenseAccount: ExpenseAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExpenseAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ExpenseAccountUpdateComponent,
    resolve: {
      expenseAccount: ExpenseAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExpenseAccounts'
    },
    canActivate: [UserRouteAccessService]
  }
];

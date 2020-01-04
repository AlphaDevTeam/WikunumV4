import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICostOfSalesAccount, CostOfSalesAccount } from 'app/shared/model/cost-of-sales-account.model';
import { CostOfSalesAccountService } from './cost-of-sales-account.service';
import { CostOfSalesAccountComponent } from './cost-of-sales-account.component';
import { CostOfSalesAccountDetailComponent } from './cost-of-sales-account-detail.component';
import { CostOfSalesAccountUpdateComponent } from './cost-of-sales-account-update.component';

@Injectable({ providedIn: 'root' })
export class CostOfSalesAccountResolve implements Resolve<ICostOfSalesAccount> {
  constructor(private service: CostOfSalesAccountService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICostOfSalesAccount> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((costOfSalesAccount: HttpResponse<CostOfSalesAccount>) => {
          if (costOfSalesAccount.body) {
            return of(costOfSalesAccount.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CostOfSalesAccount());
  }
}

export const costOfSalesAccountRoute: Routes = [
  {
    path: '',
    component: CostOfSalesAccountComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'CostOfSalesAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CostOfSalesAccountDetailComponent,
    resolve: {
      costOfSalesAccount: CostOfSalesAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CostOfSalesAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CostOfSalesAccountUpdateComponent,
    resolve: {
      costOfSalesAccount: CostOfSalesAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CostOfSalesAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CostOfSalesAccountUpdateComponent,
    resolve: {
      costOfSalesAccount: CostOfSalesAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CostOfSalesAccounts'
    },
    canActivate: [UserRouteAccessService]
  }
];

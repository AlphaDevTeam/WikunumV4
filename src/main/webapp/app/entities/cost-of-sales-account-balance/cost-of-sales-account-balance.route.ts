import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICostOfSalesAccountBalance, CostOfSalesAccountBalance } from 'app/shared/model/cost-of-sales-account-balance.model';
import { CostOfSalesAccountBalanceService } from './cost-of-sales-account-balance.service';
import { CostOfSalesAccountBalanceComponent } from './cost-of-sales-account-balance.component';
import { CostOfSalesAccountBalanceDetailComponent } from './cost-of-sales-account-balance-detail.component';
import { CostOfSalesAccountBalanceUpdateComponent } from './cost-of-sales-account-balance-update.component';

@Injectable({ providedIn: 'root' })
export class CostOfSalesAccountBalanceResolve implements Resolve<ICostOfSalesAccountBalance> {
  constructor(private service: CostOfSalesAccountBalanceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICostOfSalesAccountBalance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((costOfSalesAccountBalance: HttpResponse<CostOfSalesAccountBalance>) => {
          if (costOfSalesAccountBalance.body) {
            return of(costOfSalesAccountBalance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CostOfSalesAccountBalance());
  }
}

export const costOfSalesAccountBalanceRoute: Routes = [
  {
    path: '',
    component: CostOfSalesAccountBalanceComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'CostOfSalesAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CostOfSalesAccountBalanceDetailComponent,
    resolve: {
      costOfSalesAccountBalance: CostOfSalesAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CostOfSalesAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CostOfSalesAccountBalanceUpdateComponent,
    resolve: {
      costOfSalesAccountBalance: CostOfSalesAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CostOfSalesAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CostOfSalesAccountBalanceUpdateComponent,
    resolve: {
      costOfSalesAccountBalance: CostOfSalesAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CostOfSalesAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  }
];

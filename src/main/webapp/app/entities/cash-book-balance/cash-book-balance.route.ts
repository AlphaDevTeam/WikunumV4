import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICashBookBalance, CashBookBalance } from 'app/shared/model/cash-book-balance.model';
import { CashBookBalanceService } from './cash-book-balance.service';
import { CashBookBalanceComponent } from './cash-book-balance.component';
import { CashBookBalanceDetailComponent } from './cash-book-balance-detail.component';
import { CashBookBalanceUpdateComponent } from './cash-book-balance-update.component';

@Injectable({ providedIn: 'root' })
export class CashBookBalanceResolve implements Resolve<ICashBookBalance> {
  constructor(private service: CashBookBalanceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICashBookBalance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((cashBookBalance: HttpResponse<CashBookBalance>) => {
          if (cashBookBalance.body) {
            return of(cashBookBalance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CashBookBalance());
  }
}

export const cashBookBalanceRoute: Routes = [
  {
    path: '',
    component: CashBookBalanceComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashBookBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CashBookBalanceDetailComponent,
    resolve: {
      cashBookBalance: CashBookBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashBookBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CashBookBalanceUpdateComponent,
    resolve: {
      cashBookBalance: CashBookBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashBookBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CashBookBalanceUpdateComponent,
    resolve: {
      cashBookBalance: CashBookBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashBookBalances'
    },
    canActivate: [UserRouteAccessService]
  }
];

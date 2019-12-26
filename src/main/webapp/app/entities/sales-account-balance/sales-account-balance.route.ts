import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISalesAccountBalance, SalesAccountBalance } from 'app/shared/model/sales-account-balance.model';
import { SalesAccountBalanceService } from './sales-account-balance.service';
import { SalesAccountBalanceComponent } from './sales-account-balance.component';
import { SalesAccountBalanceDetailComponent } from './sales-account-balance-detail.component';
import { SalesAccountBalanceUpdateComponent } from './sales-account-balance-update.component';

@Injectable({ providedIn: 'root' })
export class SalesAccountBalanceResolve implements Resolve<ISalesAccountBalance> {
  constructor(private service: SalesAccountBalanceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISalesAccountBalance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((salesAccountBalance: HttpResponse<SalesAccountBalance>) => {
          if (salesAccountBalance.body) {
            return of(salesAccountBalance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SalesAccountBalance());
  }
}

export const salesAccountBalanceRoute: Routes = [
  {
    path: '',
    component: SalesAccountBalanceComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SalesAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SalesAccountBalanceDetailComponent,
    resolve: {
      salesAccountBalance: SalesAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SalesAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SalesAccountBalanceUpdateComponent,
    resolve: {
      salesAccountBalance: SalesAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SalesAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SalesAccountBalanceUpdateComponent,
    resolve: {
      salesAccountBalance: SalesAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SalesAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPurchaseAccountBalance, PurchaseAccountBalance } from 'app/shared/model/purchase-account-balance.model';
import { PurchaseAccountBalanceService } from './purchase-account-balance.service';
import { PurchaseAccountBalanceComponent } from './purchase-account-balance.component';
import { PurchaseAccountBalanceDetailComponent } from './purchase-account-balance-detail.component';
import { PurchaseAccountBalanceUpdateComponent } from './purchase-account-balance-update.component';

@Injectable({ providedIn: 'root' })
export class PurchaseAccountBalanceResolve implements Resolve<IPurchaseAccountBalance> {
  constructor(private service: PurchaseAccountBalanceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPurchaseAccountBalance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((purchaseAccountBalance: HttpResponse<PurchaseAccountBalance>) => {
          if (purchaseAccountBalance.body) {
            return of(purchaseAccountBalance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PurchaseAccountBalance());
  }
}

export const purchaseAccountBalanceRoute: Routes = [
  {
    path: '',
    component: PurchaseAccountBalanceComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PurchaseAccountBalanceDetailComponent,
    resolve: {
      purchaseAccountBalance: PurchaseAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PurchaseAccountBalanceUpdateComponent,
    resolve: {
      purchaseAccountBalance: PurchaseAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PurchaseAccountBalanceUpdateComponent,
    resolve: {
      purchaseAccountBalance: PurchaseAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  }
];

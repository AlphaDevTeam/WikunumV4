import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPurchaseAccount, PurchaseAccount } from 'app/shared/model/purchase-account.model';
import { PurchaseAccountService } from './purchase-account.service';
import { PurchaseAccountComponent } from './purchase-account.component';
import { PurchaseAccountDetailComponent } from './purchase-account-detail.component';
import { PurchaseAccountUpdateComponent } from './purchase-account-update.component';

@Injectable({ providedIn: 'root' })
export class PurchaseAccountResolve implements Resolve<IPurchaseAccount> {
  constructor(private service: PurchaseAccountService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPurchaseAccount> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((purchaseAccount: HttpResponse<PurchaseAccount>) => {
          if (purchaseAccount.body) {
            return of(purchaseAccount.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PurchaseAccount());
  }
}

export const purchaseAccountRoute: Routes = [
  {
    path: '',
    component: PurchaseAccountComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PurchaseAccountDetailComponent,
    resolve: {
      purchaseAccount: PurchaseAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PurchaseAccountUpdateComponent,
    resolve: {
      purchaseAccount: PurchaseAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PurchaseAccountUpdateComponent,
    resolve: {
      purchaseAccount: PurchaseAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseAccounts'
    },
    canActivate: [UserRouteAccessService]
  }
];

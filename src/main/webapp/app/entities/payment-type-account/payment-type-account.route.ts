import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPaymentTypeAccount, PaymentTypeAccount } from 'app/shared/model/payment-type-account.model';
import { PaymentTypeAccountService } from './payment-type-account.service';
import { PaymentTypeAccountComponent } from './payment-type-account.component';
import { PaymentTypeAccountDetailComponent } from './payment-type-account-detail.component';
import { PaymentTypeAccountUpdateComponent } from './payment-type-account-update.component';

@Injectable({ providedIn: 'root' })
export class PaymentTypeAccountResolve implements Resolve<IPaymentTypeAccount> {
  constructor(private service: PaymentTypeAccountService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentTypeAccount> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((paymentTypeAccount: HttpResponse<PaymentTypeAccount>) => {
          if (paymentTypeAccount.body) {
            return of(paymentTypeAccount.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PaymentTypeAccount());
  }
}

export const paymentTypeAccountRoute: Routes = [
  {
    path: '',
    component: PaymentTypeAccountComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PaymentTypeAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PaymentTypeAccountDetailComponent,
    resolve: {
      paymentTypeAccount: PaymentTypeAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PaymentTypeAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PaymentTypeAccountUpdateComponent,
    resolve: {
      paymentTypeAccount: PaymentTypeAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PaymentTypeAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PaymentTypeAccountUpdateComponent,
    resolve: {
      paymentTypeAccount: PaymentTypeAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PaymentTypeAccounts'
    },
    canActivate: [UserRouteAccessService]
  }
];

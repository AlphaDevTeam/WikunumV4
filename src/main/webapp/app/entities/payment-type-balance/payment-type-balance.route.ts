import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPaymentTypeBalance, PaymentTypeBalance } from 'app/shared/model/payment-type-balance.model';
import { PaymentTypeBalanceService } from './payment-type-balance.service';
import { PaymentTypeBalanceComponent } from './payment-type-balance.component';
import { PaymentTypeBalanceDetailComponent } from './payment-type-balance-detail.component';
import { PaymentTypeBalanceUpdateComponent } from './payment-type-balance-update.component';

@Injectable({ providedIn: 'root' })
export class PaymentTypeBalanceResolve implements Resolve<IPaymentTypeBalance> {
  constructor(private service: PaymentTypeBalanceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentTypeBalance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((paymentTypeBalance: HttpResponse<PaymentTypeBalance>) => {
          if (paymentTypeBalance.body) {
            return of(paymentTypeBalance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PaymentTypeBalance());
  }
}

export const paymentTypeBalanceRoute: Routes = [
  {
    path: '',
    component: PaymentTypeBalanceComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PaymentTypeBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PaymentTypeBalanceDetailComponent,
    resolve: {
      paymentTypeBalance: PaymentTypeBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PaymentTypeBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PaymentTypeBalanceUpdateComponent,
    resolve: {
      paymentTypeBalance: PaymentTypeBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PaymentTypeBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PaymentTypeBalanceUpdateComponent,
    resolve: {
      paymentTypeBalance: PaymentTypeBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PaymentTypeBalances'
    },
    canActivate: [UserRouteAccessService]
  }
];

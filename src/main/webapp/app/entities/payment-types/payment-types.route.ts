import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPaymentTypes, PaymentTypes } from 'app/shared/model/payment-types.model';
import { PaymentTypesService } from './payment-types.service';
import { PaymentTypesComponent } from './payment-types.component';
import { PaymentTypesDetailComponent } from './payment-types-detail.component';
import { PaymentTypesUpdateComponent } from './payment-types-update.component';

@Injectable({ providedIn: 'root' })
export class PaymentTypesResolve implements Resolve<IPaymentTypes> {
  constructor(private service: PaymentTypesService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentTypes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((paymentTypes: HttpResponse<PaymentTypes>) => {
          if (paymentTypes.body) {
            return of(paymentTypes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PaymentTypes());
  }
}

export const paymentTypesRoute: Routes = [
  {
    path: '',
    component: PaymentTypesComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PaymentTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PaymentTypesDetailComponent,
    resolve: {
      paymentTypes: PaymentTypesResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PaymentTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PaymentTypesUpdateComponent,
    resolve: {
      paymentTypes: PaymentTypesResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PaymentTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PaymentTypesUpdateComponent,
    resolve: {
      paymentTypes: PaymentTypesResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PaymentTypes'
    },
    canActivate: [UserRouteAccessService]
  }
];

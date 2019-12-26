import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPurchaseOrderDetails, PurchaseOrderDetails } from 'app/shared/model/purchase-order-details.model';
import { PurchaseOrderDetailsService } from './purchase-order-details.service';
import { PurchaseOrderDetailsComponent } from './purchase-order-details.component';
import { PurchaseOrderDetailsDetailComponent } from './purchase-order-details-detail.component';
import { PurchaseOrderDetailsUpdateComponent } from './purchase-order-details-update.component';

@Injectable({ providedIn: 'root' })
export class PurchaseOrderDetailsResolve implements Resolve<IPurchaseOrderDetails> {
  constructor(private service: PurchaseOrderDetailsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPurchaseOrderDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((purchaseOrderDetails: HttpResponse<PurchaseOrderDetails>) => {
          if (purchaseOrderDetails.body) {
            return of(purchaseOrderDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PurchaseOrderDetails());
  }
}

export const purchaseOrderDetailsRoute: Routes = [
  {
    path: '',
    component: PurchaseOrderDetailsComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseOrderDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PurchaseOrderDetailsDetailComponent,
    resolve: {
      purchaseOrderDetails: PurchaseOrderDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseOrderDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PurchaseOrderDetailsUpdateComponent,
    resolve: {
      purchaseOrderDetails: PurchaseOrderDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseOrderDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PurchaseOrderDetailsUpdateComponent,
    resolve: {
      purchaseOrderDetails: PurchaseOrderDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PurchaseOrderDetails'
    },
    canActivate: [UserRouteAccessService]
  }
];

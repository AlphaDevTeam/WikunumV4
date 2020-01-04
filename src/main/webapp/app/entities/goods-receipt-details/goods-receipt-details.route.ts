import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IGoodsReceiptDetails, GoodsReceiptDetails } from 'app/shared/model/goods-receipt-details.model';
import { GoodsReceiptDetailsService } from './goods-receipt-details.service';
import { GoodsReceiptDetailsComponent } from './goods-receipt-details.component';
import { GoodsReceiptDetailsDetailComponent } from './goods-receipt-details-detail.component';
import { GoodsReceiptDetailsUpdateComponent } from './goods-receipt-details-update.component';

@Injectable({ providedIn: 'root' })
export class GoodsReceiptDetailsResolve implements Resolve<IGoodsReceiptDetails> {
  constructor(private service: GoodsReceiptDetailsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGoodsReceiptDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((goodsReceiptDetails: HttpResponse<GoodsReceiptDetails>) => {
          if (goodsReceiptDetails.body) {
            return of(goodsReceiptDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new GoodsReceiptDetails());
  }
}

export const goodsReceiptDetailsRoute: Routes = [
  {
    path: '',
    component: GoodsReceiptDetailsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'GoodsReceiptDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: GoodsReceiptDetailsDetailComponent,
    resolve: {
      goodsReceiptDetails: GoodsReceiptDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GoodsReceiptDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: GoodsReceiptDetailsUpdateComponent,
    resolve: {
      goodsReceiptDetails: GoodsReceiptDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GoodsReceiptDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: GoodsReceiptDetailsUpdateComponent,
    resolve: {
      goodsReceiptDetails: GoodsReceiptDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GoodsReceiptDetails'
    },
    canActivate: [UserRouteAccessService]
  }
];

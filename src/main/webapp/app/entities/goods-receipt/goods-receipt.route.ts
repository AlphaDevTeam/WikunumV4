import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IGoodsReceipt, GoodsReceipt } from 'app/shared/model/goods-receipt.model';
import { GoodsReceiptService } from './goods-receipt.service';
import { GoodsReceiptComponent } from './goods-receipt.component';
import { GoodsReceiptDetailComponent } from './goods-receipt-detail.component';
import { GoodsReceiptUpdateComponent } from './goods-receipt-update.component';

@Injectable({ providedIn: 'root' })
export class GoodsReceiptResolve implements Resolve<IGoodsReceipt> {
  constructor(private service: GoodsReceiptService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGoodsReceipt> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((goodsReceipt: HttpResponse<GoodsReceipt>) => {
          if (goodsReceipt.body) {
            return of(goodsReceipt.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new GoodsReceipt());
  }
}

export const goodsReceiptRoute: Routes = [
  {
    path: '',
    component: GoodsReceiptComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'GoodsReceipts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: GoodsReceiptDetailComponent,
    resolve: {
      goodsReceipt: GoodsReceiptResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GoodsReceipts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: GoodsReceiptUpdateComponent,
    resolve: {
      goodsReceipt: GoodsReceiptResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GoodsReceipts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: GoodsReceiptUpdateComponent,
    resolve: {
      goodsReceipt: GoodsReceiptResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GoodsReceipts'
    },
    canActivate: [UserRouteAccessService]
  }
];

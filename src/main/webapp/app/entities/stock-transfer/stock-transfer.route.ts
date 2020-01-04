import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IStockTransfer, StockTransfer } from 'app/shared/model/stock-transfer.model';
import { StockTransferService } from './stock-transfer.service';
import { StockTransferComponent } from './stock-transfer.component';
import { StockTransferDetailComponent } from './stock-transfer-detail.component';
import { StockTransferUpdateComponent } from './stock-transfer-update.component';

@Injectable({ providedIn: 'root' })
export class StockTransferResolve implements Resolve<IStockTransfer> {
  constructor(private service: StockTransferService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStockTransfer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((stockTransfer: HttpResponse<StockTransfer>) => {
          if (stockTransfer.body) {
            return of(stockTransfer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StockTransfer());
  }
}

export const stockTransferRoute: Routes = [
  {
    path: '',
    component: StockTransferComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'StockTransfers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: StockTransferDetailComponent,
    resolve: {
      stockTransfer: StockTransferResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'StockTransfers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: StockTransferUpdateComponent,
    resolve: {
      stockTransfer: StockTransferResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'StockTransfers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: StockTransferUpdateComponent,
    resolve: {
      stockTransfer: StockTransferResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'StockTransfers'
    },
    canActivate: [UserRouteAccessService]
  }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICashPaymentVoucherSupplier, CashPaymentVoucherSupplier } from 'app/shared/model/cash-payment-voucher-supplier.model';
import { CashPaymentVoucherSupplierService } from './cash-payment-voucher-supplier.service';
import { CashPaymentVoucherSupplierComponent } from './cash-payment-voucher-supplier.component';
import { CashPaymentVoucherSupplierDetailComponent } from './cash-payment-voucher-supplier-detail.component';
import { CashPaymentVoucherSupplierUpdateComponent } from './cash-payment-voucher-supplier-update.component';

@Injectable({ providedIn: 'root' })
export class CashPaymentVoucherSupplierResolve implements Resolve<ICashPaymentVoucherSupplier> {
  constructor(private service: CashPaymentVoucherSupplierService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICashPaymentVoucherSupplier> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((cashPaymentVoucherSupplier: HttpResponse<CashPaymentVoucherSupplier>) => {
          if (cashPaymentVoucherSupplier.body) {
            return of(cashPaymentVoucherSupplier.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CashPaymentVoucherSupplier());
  }
}

export const cashPaymentVoucherSupplierRoute: Routes = [
  {
    path: '',
    component: CashPaymentVoucherSupplierComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'CashPaymentVoucherSuppliers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CashPaymentVoucherSupplierDetailComponent,
    resolve: {
      cashPaymentVoucherSupplier: CashPaymentVoucherSupplierResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashPaymentVoucherSuppliers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CashPaymentVoucherSupplierUpdateComponent,
    resolve: {
      cashPaymentVoucherSupplier: CashPaymentVoucherSupplierResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashPaymentVoucherSuppliers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CashPaymentVoucherSupplierUpdateComponent,
    resolve: {
      cashPaymentVoucherSupplier: CashPaymentVoucherSupplierResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashPaymentVoucherSuppliers'
    },
    canActivate: [UserRouteAccessService]
  }
];

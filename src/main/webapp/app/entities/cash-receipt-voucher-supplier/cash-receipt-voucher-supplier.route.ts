import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICashReceiptVoucherSupplier, CashReceiptVoucherSupplier } from 'app/shared/model/cash-receipt-voucher-supplier.model';
import { CashReceiptVoucherSupplierService } from './cash-receipt-voucher-supplier.service';
import { CashReceiptVoucherSupplierComponent } from './cash-receipt-voucher-supplier.component';
import { CashReceiptVoucherSupplierDetailComponent } from './cash-receipt-voucher-supplier-detail.component';
import { CashReceiptVoucherSupplierUpdateComponent } from './cash-receipt-voucher-supplier-update.component';

@Injectable({ providedIn: 'root' })
export class CashReceiptVoucherSupplierResolve implements Resolve<ICashReceiptVoucherSupplier> {
  constructor(private service: CashReceiptVoucherSupplierService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICashReceiptVoucherSupplier> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((cashReceiptVoucherSupplier: HttpResponse<CashReceiptVoucherSupplier>) => {
          if (cashReceiptVoucherSupplier.body) {
            return of(cashReceiptVoucherSupplier.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CashReceiptVoucherSupplier());
  }
}

export const cashReceiptVoucherSupplierRoute: Routes = [
  {
    path: '',
    component: CashReceiptVoucherSupplierComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashReceiptVoucherSuppliers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CashReceiptVoucherSupplierDetailComponent,
    resolve: {
      cashReceiptVoucherSupplier: CashReceiptVoucherSupplierResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashReceiptVoucherSuppliers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CashReceiptVoucherSupplierUpdateComponent,
    resolve: {
      cashReceiptVoucherSupplier: CashReceiptVoucherSupplierResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashReceiptVoucherSuppliers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CashReceiptVoucherSupplierUpdateComponent,
    resolve: {
      cashReceiptVoucherSupplier: CashReceiptVoucherSupplierResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashReceiptVoucherSuppliers'
    },
    canActivate: [UserRouteAccessService]
  }
];

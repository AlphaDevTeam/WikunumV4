import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISupplierAccountBalance, SupplierAccountBalance } from 'app/shared/model/supplier-account-balance.model';
import { SupplierAccountBalanceService } from './supplier-account-balance.service';
import { SupplierAccountBalanceComponent } from './supplier-account-balance.component';
import { SupplierAccountBalanceDetailComponent } from './supplier-account-balance-detail.component';
import { SupplierAccountBalanceUpdateComponent } from './supplier-account-balance-update.component';

@Injectable({ providedIn: 'root' })
export class SupplierAccountBalanceResolve implements Resolve<ISupplierAccountBalance> {
  constructor(private service: SupplierAccountBalanceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISupplierAccountBalance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((supplierAccountBalance: HttpResponse<SupplierAccountBalance>) => {
          if (supplierAccountBalance.body) {
            return of(supplierAccountBalance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SupplierAccountBalance());
  }
}

export const supplierAccountBalanceRoute: Routes = [
  {
    path: '',
    component: SupplierAccountBalanceComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'SupplierAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SupplierAccountBalanceDetailComponent,
    resolve: {
      supplierAccountBalance: SupplierAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SupplierAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SupplierAccountBalanceUpdateComponent,
    resolve: {
      supplierAccountBalance: SupplierAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SupplierAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SupplierAccountBalanceUpdateComponent,
    resolve: {
      supplierAccountBalance: SupplierAccountBalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SupplierAccountBalances'
    },
    canActivate: [UserRouteAccessService]
  }
];

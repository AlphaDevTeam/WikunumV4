import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISupplierAccount, SupplierAccount } from 'app/shared/model/supplier-account.model';
import { SupplierAccountService } from './supplier-account.service';
import { SupplierAccountComponent } from './supplier-account.component';
import { SupplierAccountDetailComponent } from './supplier-account-detail.component';
import { SupplierAccountUpdateComponent } from './supplier-account-update.component';

@Injectable({ providedIn: 'root' })
export class SupplierAccountResolve implements Resolve<ISupplierAccount> {
  constructor(private service: SupplierAccountService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISupplierAccount> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((supplierAccount: HttpResponse<SupplierAccount>) => {
          if (supplierAccount.body) {
            return of(supplierAccount.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SupplierAccount());
  }
}

export const supplierAccountRoute: Routes = [
  {
    path: '',
    component: SupplierAccountComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SupplierAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SupplierAccountDetailComponent,
    resolve: {
      supplierAccount: SupplierAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SupplierAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SupplierAccountUpdateComponent,
    resolve: {
      supplierAccount: SupplierAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SupplierAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SupplierAccountUpdateComponent,
    resolve: {
      supplierAccount: SupplierAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SupplierAccounts'
    },
    canActivate: [UserRouteAccessService]
  }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISalesAccount, SalesAccount } from 'app/shared/model/sales-account.model';
import { SalesAccountService } from './sales-account.service';
import { SalesAccountComponent } from './sales-account.component';
import { SalesAccountDetailComponent } from './sales-account-detail.component';
import { SalesAccountUpdateComponent } from './sales-account-update.component';

@Injectable({ providedIn: 'root' })
export class SalesAccountResolve implements Resolve<ISalesAccount> {
  constructor(private service: SalesAccountService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISalesAccount> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((salesAccount: HttpResponse<SalesAccount>) => {
          if (salesAccount.body) {
            return of(salesAccount.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SalesAccount());
  }
}

export const salesAccountRoute: Routes = [
  {
    path: '',
    component: SalesAccountComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'SalesAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SalesAccountDetailComponent,
    resolve: {
      salesAccount: SalesAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SalesAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SalesAccountUpdateComponent,
    resolve: {
      salesAccount: SalesAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SalesAccounts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SalesAccountUpdateComponent,
    resolve: {
      salesAccount: SalesAccountResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SalesAccounts'
    },
    canActivate: [UserRouteAccessService]
  }
];

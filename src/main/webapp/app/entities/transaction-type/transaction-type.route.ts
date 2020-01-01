import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITransactionType, TransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from './transaction-type.service';
import { TransactionTypeComponent } from './transaction-type.component';
import { TransactionTypeDetailComponent } from './transaction-type-detail.component';
import { TransactionTypeUpdateComponent } from './transaction-type-update.component';

@Injectable({ providedIn: 'root' })
export class TransactionTypeResolve implements Resolve<ITransactionType> {
  constructor(private service: TransactionTypeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITransactionType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((transactionType: HttpResponse<TransactionType>) => {
          if (transactionType.body) {
            return of(transactionType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TransactionType());
  }
}

export const transactionTypeRoute: Routes = [
  {
    path: '',
    component: TransactionTypeComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'TransactionTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TransactionTypeDetailComponent,
    resolve: {
      transactionType: TransactionTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TransactionTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TransactionTypeUpdateComponent,
    resolve: {
      transactionType: TransactionTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TransactionTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TransactionTypeUpdateComponent,
    resolve: {
      transactionType: TransactionTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TransactionTypes'
    },
    canActivate: [UserRouteAccessService]
  }
];

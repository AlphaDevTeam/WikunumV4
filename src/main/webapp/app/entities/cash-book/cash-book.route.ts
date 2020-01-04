import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICashBook, CashBook } from 'app/shared/model/cash-book.model';
import { CashBookService } from './cash-book.service';
import { CashBookComponent } from './cash-book.component';
import { CashBookDetailComponent } from './cash-book-detail.component';
import { CashBookUpdateComponent } from './cash-book-update.component';

@Injectable({ providedIn: 'root' })
export class CashBookResolve implements Resolve<ICashBook> {
  constructor(private service: CashBookService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICashBook> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((cashBook: HttpResponse<CashBook>) => {
          if (cashBook.body) {
            return of(cashBook.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CashBook());
  }
}

export const cashBookRoute: Routes = [
  {
    path: '',
    component: CashBookComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'CashBooks'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CashBookDetailComponent,
    resolve: {
      cashBook: CashBookResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashBooks'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CashBookUpdateComponent,
    resolve: {
      cashBook: CashBookResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashBooks'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CashBookUpdateComponent,
    resolve: {
      cashBook: CashBookResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashBooks'
    },
    canActivate: [UserRouteAccessService]
  }
];

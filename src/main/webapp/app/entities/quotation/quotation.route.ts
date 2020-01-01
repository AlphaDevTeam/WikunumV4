import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IQuotation, Quotation } from 'app/shared/model/quotation.model';
import { QuotationService } from './quotation.service';
import { QuotationComponent } from './quotation.component';
import { QuotationDetailComponent } from './quotation-detail.component';
import { QuotationUpdateComponent } from './quotation-update.component';

@Injectable({ providedIn: 'root' })
export class QuotationResolve implements Resolve<IQuotation> {
  constructor(private service: QuotationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQuotation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((quotation: HttpResponse<Quotation>) => {
          if (quotation.body) {
            return of(quotation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Quotation());
  }
}

export const quotationRoute: Routes = [
  {
    path: '',
    component: QuotationComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Quotations'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: QuotationDetailComponent,
    resolve: {
      quotation: QuotationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Quotations'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: QuotationUpdateComponent,
    resolve: {
      quotation: QuotationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Quotations'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: QuotationUpdateComponent,
    resolve: {
      quotation: QuotationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Quotations'
    },
    canActivate: [UserRouteAccessService]
  }
];

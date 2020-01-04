import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IQuotationDetails, QuotationDetails } from 'app/shared/model/quotation-details.model';
import { QuotationDetailsService } from './quotation-details.service';
import { QuotationDetailsComponent } from './quotation-details.component';
import { QuotationDetailsDetailComponent } from './quotation-details-detail.component';
import { QuotationDetailsUpdateComponent } from './quotation-details-update.component';

@Injectable({ providedIn: 'root' })
export class QuotationDetailsResolve implements Resolve<IQuotationDetails> {
  constructor(private service: QuotationDetailsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQuotationDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((quotationDetails: HttpResponse<QuotationDetails>) => {
          if (quotationDetails.body) {
            return of(quotationDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new QuotationDetails());
  }
}

export const quotationDetailsRoute: Routes = [
  {
    path: '',
    component: QuotationDetailsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'QuotationDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: QuotationDetailsDetailComponent,
    resolve: {
      quotationDetails: QuotationDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'QuotationDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: QuotationDetailsUpdateComponent,
    resolve: {
      quotationDetails: QuotationDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'QuotationDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: QuotationDetailsUpdateComponent,
    resolve: {
      quotationDetails: QuotationDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'QuotationDetails'
    },
    canActivate: [UserRouteAccessService]
  }
];

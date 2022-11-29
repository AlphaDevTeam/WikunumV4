import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IInvoiceDetails, InvoiceDetails } from 'app/shared/model/invoice-details.model';
import { InvoiceDetailsService } from './invoice-details.service';
import { InvoiceDetailsComponent } from './invoice-details.component';
import { InvoiceDetailsDetailComponent } from './invoice-details-detail.component';
import { InvoiceDetailsUpdateComponent } from './invoice-details-update.component';

@Injectable({ providedIn: 'root' })
export class InvoiceDetailsResolve implements Resolve<IInvoiceDetails> {
  constructor(private service: InvoiceDetailsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInvoiceDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((invoiceDetails: HttpResponse<InvoiceDetails>) => {
          if (invoiceDetails.body) {
            return of(invoiceDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InvoiceDetails());
  }
}

export const invoiceDetailsRoute: Routes = [
  {
    path: '',
    component: InvoiceDetailsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'InvoiceDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: InvoiceDetailsDetailComponent,
    resolve: {
      invoiceDetails: InvoiceDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'InvoiceDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: InvoiceDetailsUpdateComponent,
    resolve: {
      invoiceDetails: InvoiceDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'InvoiceDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: InvoiceDetailsUpdateComponent,
    resolve: {
      invoiceDetails: InvoiceDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'InvoiceDetails'
    },
    canActivate: [UserRouteAccessService]
  }
];

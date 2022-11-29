import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISupplier, Supplier } from 'app/shared/model/supplier.model';
import { SupplierService } from './supplier.service';
import { SupplierComponent } from './supplier.component';
import { SupplierDetailComponent } from './supplier-detail.component';
import { SupplierUpdateComponent } from './supplier-update.component';

@Injectable({ providedIn: 'root' })
export class SupplierResolve implements Resolve<ISupplier> {
  constructor(private service: SupplierService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISupplier> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((supplier: HttpResponse<Supplier>) => {
          if (supplier.body) {
            return of(supplier.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Supplier());
  }
}

export const supplierRoute: Routes = [
  {
    path: '',
    component: SupplierComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Suppliers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SupplierDetailComponent,
    resolve: {
      supplier: SupplierResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Suppliers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SupplierUpdateComponent,
    resolve: {
      supplier: SupplierResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Suppliers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SupplierUpdateComponent,
    resolve: {
      supplier: SupplierResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Suppliers'
    },
    canActivate: [UserRouteAccessService]
  }
];

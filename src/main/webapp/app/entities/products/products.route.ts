import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IProducts, Products } from 'app/shared/model/products.model';
import { ProductsService } from './products.service';
import { ProductsComponent } from './products.component';
import { ProductsDetailComponent } from './products-detail.component';
import { ProductsUpdateComponent } from './products-update.component';

@Injectable({ providedIn: 'root' })
export class ProductsResolve implements Resolve<IProducts> {
  constructor(private service: ProductsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProducts> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((products: HttpResponse<Products>) => {
          if (products.body) {
            return of(products.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Products());
  }
}

export const productsRoute: Routes = [
  {
    path: '',
    component: ProductsComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Products'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ProductsDetailComponent,
    resolve: {
      products: ProductsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Products'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ProductsUpdateComponent,
    resolve: {
      products: ProductsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Products'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ProductsUpdateComponent,
    resolve: {
      products: ProductsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Products'
    },
    canActivate: [UserRouteAccessService]
  }
];

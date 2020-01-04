import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IItems, Items } from 'app/shared/model/items.model';
import { ItemsService } from './items.service';
import { ItemsComponent } from './items.component';
import { ItemsDetailComponent } from './items-detail.component';
import { ItemsUpdateComponent } from './items-update.component';

@Injectable({ providedIn: 'root' })
export class ItemsResolve implements Resolve<IItems> {
  constructor(private service: ItemsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItems> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((items: HttpResponse<Items>) => {
          if (items.body) {
            return of(items.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Items());
  }
}

export const itemsRoute: Routes = [
  {
    path: '',
    component: ItemsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Items'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ItemsDetailComponent,
    resolve: {
      items: ItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Items'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ItemsUpdateComponent,
    resolve: {
      items: ItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Items'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ItemsUpdateComponent,
    resolve: {
      items: ItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Items'
    },
    canActivate: [UserRouteAccessService]
  }
];

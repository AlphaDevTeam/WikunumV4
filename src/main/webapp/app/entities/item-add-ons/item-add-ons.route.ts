import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IItemAddOns, ItemAddOns } from 'app/shared/model/item-add-ons.model';
import { ItemAddOnsService } from './item-add-ons.service';
import { ItemAddOnsComponent } from './item-add-ons.component';
import { ItemAddOnsDetailComponent } from './item-add-ons-detail.component';
import { ItemAddOnsUpdateComponent } from './item-add-ons-update.component';

@Injectable({ providedIn: 'root' })
export class ItemAddOnsResolve implements Resolve<IItemAddOns> {
  constructor(private service: ItemAddOnsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItemAddOns> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((itemAddOns: HttpResponse<ItemAddOns>) => {
          if (itemAddOns.body) {
            return of(itemAddOns.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ItemAddOns());
  }
}

export const itemAddOnsRoute: Routes = [
  {
    path: '',
    component: ItemAddOnsComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ItemAddOns'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ItemAddOnsDetailComponent,
    resolve: {
      itemAddOns: ItemAddOnsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ItemAddOns'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ItemAddOnsUpdateComponent,
    resolve: {
      itemAddOns: ItemAddOnsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ItemAddOns'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ItemAddOnsUpdateComponent,
    resolve: {
      itemAddOns: ItemAddOnsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ItemAddOns'
    },
    canActivate: [UserRouteAccessService]
  }
];

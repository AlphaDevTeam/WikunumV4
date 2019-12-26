import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IItemBinCard, ItemBinCard } from 'app/shared/model/item-bin-card.model';
import { ItemBinCardService } from './item-bin-card.service';
import { ItemBinCardComponent } from './item-bin-card.component';
import { ItemBinCardDetailComponent } from './item-bin-card-detail.component';
import { ItemBinCardUpdateComponent } from './item-bin-card-update.component';

@Injectable({ providedIn: 'root' })
export class ItemBinCardResolve implements Resolve<IItemBinCard> {
  constructor(private service: ItemBinCardService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItemBinCard> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((itemBinCard: HttpResponse<ItemBinCard>) => {
          if (itemBinCard.body) {
            return of(itemBinCard.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ItemBinCard());
  }
}

export const itemBinCardRoute: Routes = [
  {
    path: '',
    component: ItemBinCardComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ItemBinCards'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ItemBinCardDetailComponent,
    resolve: {
      itemBinCard: ItemBinCardResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ItemBinCards'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ItemBinCardUpdateComponent,
    resolve: {
      itemBinCard: ItemBinCardResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ItemBinCards'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ItemBinCardUpdateComponent,
    resolve: {
      itemBinCard: ItemBinCardResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ItemBinCards'
    },
    canActivate: [UserRouteAccessService]
  }
];

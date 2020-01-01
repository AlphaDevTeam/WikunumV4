import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMenuItems, MenuItems } from 'app/shared/model/menu-items.model';
import { MenuItemsService } from './menu-items.service';
import { MenuItemsComponent } from './menu-items.component';
import { MenuItemsDetailComponent } from './menu-items-detail.component';
import { MenuItemsUpdateComponent } from './menu-items-update.component';

@Injectable({ providedIn: 'root' })
export class MenuItemsResolve implements Resolve<IMenuItems> {
  constructor(private service: MenuItemsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMenuItems> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((menuItems: HttpResponse<MenuItems>) => {
          if (menuItems.body) {
            return of(menuItems.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MenuItems());
  }
}

export const menuItemsRoute: Routes = [
  {
    path: '',
    component: MenuItemsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'MenuItems'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MenuItemsDetailComponent,
    resolve: {
      menuItems: MenuItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'MenuItems'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MenuItemsUpdateComponent,
    resolve: {
      menuItems: MenuItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'MenuItems'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MenuItemsUpdateComponent,
    resolve: {
      menuItems: MenuItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'MenuItems'
    },
    canActivate: [UserRouteAccessService]
  }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IConfigurationItems, ConfigurationItems } from 'app/shared/model/configuration-items.model';
import { ConfigurationItemsService } from './configuration-items.service';
import { ConfigurationItemsComponent } from './configuration-items.component';
import { ConfigurationItemsDetailComponent } from './configuration-items-detail.component';
import { ConfigurationItemsUpdateComponent } from './configuration-items-update.component';

@Injectable({ providedIn: 'root' })
export class ConfigurationItemsResolve implements Resolve<IConfigurationItems> {
  constructor(private service: ConfigurationItemsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConfigurationItems> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((configurationItems: HttpResponse<ConfigurationItems>) => {
          if (configurationItems.body) {
            return of(configurationItems.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ConfigurationItems());
  }
}

export const configurationItemsRoute: Routes = [
  {
    path: '',
    component: ConfigurationItemsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'ConfigurationItems'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ConfigurationItemsDetailComponent,
    resolve: {
      configurationItems: ConfigurationItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ConfigurationItems'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ConfigurationItemsUpdateComponent,
    resolve: {
      configurationItems: ConfigurationItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ConfigurationItems'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ConfigurationItemsUpdateComponent,
    resolve: {
      configurationItems: ConfigurationItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ConfigurationItems'
    },
    canActivate: [UserRouteAccessService]
  }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IUserPermissions, UserPermissions } from 'app/shared/model/user-permissions.model';
import { UserPermissionsService } from './user-permissions.service';
import { UserPermissionsComponent } from './user-permissions.component';
import { UserPermissionsDetailComponent } from './user-permissions-detail.component';
import { UserPermissionsUpdateComponent } from './user-permissions-update.component';

@Injectable({ providedIn: 'root' })
export class UserPermissionsResolve implements Resolve<IUserPermissions> {
  constructor(private service: UserPermissionsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserPermissions> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((userPermissions: HttpResponse<UserPermissions>) => {
          if (userPermissions.body) {
            return of(userPermissions.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UserPermissions());
  }
}

export const userPermissionsRoute: Routes = [
  {
    path: '',
    component: UserPermissionsComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UserPermissions'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: UserPermissionsDetailComponent,
    resolve: {
      userPermissions: UserPermissionsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UserPermissions'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: UserPermissionsUpdateComponent,
    resolve: {
      userPermissions: UserPermissionsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UserPermissions'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: UserPermissionsUpdateComponent,
    resolve: {
      userPermissions: UserPermissionsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UserPermissions'
    },
    canActivate: [UserRouteAccessService]
  }
];

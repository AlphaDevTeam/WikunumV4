import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IExUser, ExUser } from 'app/shared/model/ex-user.model';
import { ExUserService } from './ex-user.service';
import { ExUserComponent } from './ex-user.component';
import { ExUserDetailComponent } from './ex-user-detail.component';
import { ExUserUpdateComponent } from './ex-user-update.component';

@Injectable({ providedIn: 'root' })
export class ExUserResolve implements Resolve<IExUser> {
  constructor(private service: ExUserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((exUser: HttpResponse<ExUser>) => {
          if (exUser.body) {
            return of(exUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExUser());
  }
}

export const exUserRoute: Routes = [
  {
    path: '',
    component: ExUserComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ExUserDetailComponent,
    resolve: {
      exUser: ExUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ExUserUpdateComponent,
    resolve: {
      exUser: ExUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ExUserUpdateComponent,
    resolve: {
      exUser: ExUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExUsers'
    },
    canActivate: [UserRouteAccessService]
  }
];

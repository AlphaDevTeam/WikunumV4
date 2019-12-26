import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IChangeLog, ChangeLog } from 'app/shared/model/change-log.model';
import { ChangeLogService } from './change-log.service';
import { ChangeLogComponent } from './change-log.component';
import { ChangeLogDetailComponent } from './change-log-detail.component';
import { ChangeLogUpdateComponent } from './change-log-update.component';

@Injectable({ providedIn: 'root' })
export class ChangeLogResolve implements Resolve<IChangeLog> {
  constructor(private service: ChangeLogService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChangeLog> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((changeLog: HttpResponse<ChangeLog>) => {
          if (changeLog.body) {
            return of(changeLog.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ChangeLog());
  }
}

export const changeLogRoute: Routes = [
  {
    path: '',
    component: ChangeLogComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ChangeLogs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ChangeLogDetailComponent,
    resolve: {
      changeLog: ChangeLogResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ChangeLogs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ChangeLogUpdateComponent,
    resolve: {
      changeLog: ChangeLogResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ChangeLogs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ChangeLogUpdateComponent,
    resolve: {
      changeLog: ChangeLogResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ChangeLogs'
    },
    canActivate: [UserRouteAccessService]
  }
];

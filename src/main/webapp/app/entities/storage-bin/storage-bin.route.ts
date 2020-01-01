import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IStorageBin, StorageBin } from 'app/shared/model/storage-bin.model';
import { StorageBinService } from './storage-bin.service';
import { StorageBinComponent } from './storage-bin.component';
import { StorageBinDetailComponent } from './storage-bin-detail.component';
import { StorageBinUpdateComponent } from './storage-bin-update.component';

@Injectable({ providedIn: 'root' })
export class StorageBinResolve implements Resolve<IStorageBin> {
  constructor(private service: StorageBinService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStorageBin> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((storageBin: HttpResponse<StorageBin>) => {
          if (storageBin.body) {
            return of(storageBin.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StorageBin());
  }
}

export const storageBinRoute: Routes = [
  {
    path: '',
    component: StorageBinComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'StorageBins'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: StorageBinDetailComponent,
    resolve: {
      storageBin: StorageBinResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'StorageBins'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: StorageBinUpdateComponent,
    resolve: {
      storageBin: StorageBinResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'StorageBins'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: StorageBinUpdateComponent,
    resolve: {
      storageBin: StorageBinResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'StorageBins'
    },
    canActivate: [UserRouteAccessService]
  }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDocumentNumberConfig, DocumentNumberConfig } from 'app/shared/model/document-number-config.model';
import { DocumentNumberConfigService } from './document-number-config.service';
import { DocumentNumberConfigComponent } from './document-number-config.component';
import { DocumentNumberConfigDetailComponent } from './document-number-config-detail.component';
import { DocumentNumberConfigUpdateComponent } from './document-number-config-update.component';

@Injectable({ providedIn: 'root' })
export class DocumentNumberConfigResolve implements Resolve<IDocumentNumberConfig> {
  constructor(private service: DocumentNumberConfigService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentNumberConfig> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((documentNumberConfig: HttpResponse<DocumentNumberConfig>) => {
          if (documentNumberConfig.body) {
            return of(documentNumberConfig.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocumentNumberConfig());
  }
}

export const documentNumberConfigRoute: Routes = [
  {
    path: '',
    component: DocumentNumberConfigComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'DocumentNumberConfigs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DocumentNumberConfigDetailComponent,
    resolve: {
      documentNumberConfig: DocumentNumberConfigResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DocumentNumberConfigs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DocumentNumberConfigUpdateComponent,
    resolve: {
      documentNumberConfig: DocumentNumberConfigResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DocumentNumberConfigs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DocumentNumberConfigUpdateComponent,
    resolve: {
      documentNumberConfig: DocumentNumberConfigResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DocumentNumberConfigs'
    },
    canActivate: [UserRouteAccessService]
  }
];

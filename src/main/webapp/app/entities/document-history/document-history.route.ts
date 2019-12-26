import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDocumentHistory, DocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from './document-history.service';
import { DocumentHistoryComponent } from './document-history.component';
import { DocumentHistoryDetailComponent } from './document-history-detail.component';
import { DocumentHistoryUpdateComponent } from './document-history-update.component';

@Injectable({ providedIn: 'root' })
export class DocumentHistoryResolve implements Resolve<IDocumentHistory> {
  constructor(private service: DocumentHistoryService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((documentHistory: HttpResponse<DocumentHistory>) => {
          if (documentHistory.body) {
            return of(documentHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocumentHistory());
  }
}

export const documentHistoryRoute: Routes = [
  {
    path: '',
    component: DocumentHistoryComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DocumentHistories'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DocumentHistoryDetailComponent,
    resolve: {
      documentHistory: DocumentHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DocumentHistories'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DocumentHistoryUpdateComponent,
    resolve: {
      documentHistory: DocumentHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DocumentHistories'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DocumentHistoryUpdateComponent,
    resolve: {
      documentHistory: DocumentHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DocumentHistories'
    },
    canActivate: [UserRouteAccessService]
  }
];

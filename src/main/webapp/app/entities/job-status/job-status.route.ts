import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IJobStatus, JobStatus } from 'app/shared/model/job-status.model';
import { JobStatusService } from './job-status.service';
import { JobStatusComponent } from './job-status.component';
import { JobStatusDetailComponent } from './job-status-detail.component';
import { JobStatusUpdateComponent } from './job-status-update.component';

@Injectable({ providedIn: 'root' })
export class JobStatusResolve implements Resolve<IJobStatus> {
  constructor(private service: JobStatusService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IJobStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((jobStatus: HttpResponse<JobStatus>) => {
          if (jobStatus.body) {
            return of(jobStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new JobStatus());
  }
}

export const jobStatusRoute: Routes = [
  {
    path: '',
    component: JobStatusComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'JobStatuses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: JobStatusDetailComponent,
    resolve: {
      jobStatus: JobStatusResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'JobStatuses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: JobStatusUpdateComponent,
    resolve: {
      jobStatus: JobStatusResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'JobStatuses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: JobStatusUpdateComponent,
    resolve: {
      jobStatus: JobStatusResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'JobStatuses'
    },
    canActivate: [UserRouteAccessService]
  }
];

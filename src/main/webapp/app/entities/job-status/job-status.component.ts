import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IJobStatus } from 'app/shared/model/job-status.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { JobStatusService } from './job-status.service';
import { JobStatusDeleteDialogComponent } from './job-status-delete-dialog.component';

@Component({
  selector: 'jhi-job-status',
  templateUrl: './job-status.component.html'
})
export class JobStatusComponent implements OnInit, OnDestroy {
  jobStatuses: IJobStatus[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected jobStatusService: JobStatusService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.jobStatuses = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.jobStatusService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IJobStatus[]>) => this.paginateJobStatuses(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.jobStatuses = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInJobStatuses();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IJobStatus): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInJobStatuses(): void {
    this.eventSubscriber = this.eventManager.subscribe('jobStatusListModification', () => this.reset());
  }

  delete(jobStatus: IJobStatus): void {
    const modalRef = this.modalService.open(JobStatusDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.jobStatus = jobStatus;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateJobStatuses(data: IJobStatus[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.jobStatuses.push(data[i]);
      }
    }
  }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IJobDetails } from 'app/shared/model/job-details.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { JobDetailsService } from './job-details.service';
import { JobDetailsDeleteDialogComponent } from './job-details-delete-dialog.component';

@Component({
  selector: 'jhi-job-details',
  templateUrl: './job-details.component.html'
})
export class JobDetailsComponent implements OnInit, OnDestroy {
  jobDetails: IJobDetails[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected jobDetailsService: JobDetailsService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.jobDetails = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.jobDetailsService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IJobDetails[]>) => this.paginateJobDetails(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.jobDetails = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInJobDetails();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IJobDetails): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInJobDetails(): void {
    this.eventSubscriber = this.eventManager.subscribe('jobDetailsListModification', () => this.reset());
  }

  delete(jobDetails: IJobDetails): void {
    const modalRef = this.modalService.open(JobDetailsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.jobDetails = jobDetails;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateJobDetails(data: IJobDetails[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.jobDetails.push(data[i]);
      }
    }
  }
}

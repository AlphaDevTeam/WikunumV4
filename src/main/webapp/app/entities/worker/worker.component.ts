import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorker } from 'app/shared/model/worker.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { WorkerService } from './worker.service';
import { WorkerDeleteDialogComponent } from './worker-delete-dialog.component';

@Component({
  selector: 'jhi-worker',
  templateUrl: './worker.component.html'
})
export class WorkerComponent implements OnInit, OnDestroy {
  workers: IWorker[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected workerService: WorkerService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.workers = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.workerService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IWorker[]>) => this.paginateWorkers(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.workers = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInWorkers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IWorker): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInWorkers(): void {
    this.eventSubscriber = this.eventManager.subscribe('workerListModification', () => this.reset());
  }

  delete(worker: IWorker): void {
    const modalRef = this.modalService.open(WorkerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.worker = worker;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateWorkers(data: IWorker[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.workers.push(data[i]);
      }
    }
  }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IChangeLog } from 'app/shared/model/change-log.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ChangeLogService } from './change-log.service';
import { ChangeLogDeleteDialogComponent } from './change-log-delete-dialog.component';

@Component({
  selector: 'jhi-change-log',
  templateUrl: './change-log.component.html'
})
export class ChangeLogComponent implements OnInit, OnDestroy {
  changeLogs: IChangeLog[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected changeLogService: ChangeLogService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.changeLogs = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.changeLogService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IChangeLog[]>) => this.paginateChangeLogs(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.changeLogs = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInChangeLogs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IChangeLog): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInChangeLogs(): void {
    this.eventSubscriber = this.eventManager.subscribe('changeLogListModification', () => this.reset());
  }

  delete(changeLog: IChangeLog): void {
    const modalRef = this.modalService.open(ChangeLogDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.changeLog = changeLog;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateChangeLogs(data: IChangeLog[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.changeLogs.push(data[i]);
      }
    }
  }
}

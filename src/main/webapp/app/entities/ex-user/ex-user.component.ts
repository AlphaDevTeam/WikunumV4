import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IExUser } from 'app/shared/model/ex-user.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ExUserService } from './ex-user.service';
import { ExUserDeleteDialogComponent } from './ex-user-delete-dialog.component';

@Component({
  selector: 'jhi-ex-user',
  templateUrl: './ex-user.component.html'
})
export class ExUserComponent implements OnInit, OnDestroy {
  exUsers: IExUser[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected exUserService: ExUserService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.exUsers = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.exUserService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IExUser[]>) => this.paginateExUsers(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.exUsers = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInExUsers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IExUser): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInExUsers(): void {
    this.eventSubscriber = this.eventManager.subscribe('exUserListModification', () => this.reset());
  }

  delete(exUser: IExUser): void {
    const modalRef = this.modalService.open(ExUserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.exUser = exUser;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateExUsers(data: IExUser[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.exUsers.push(data[i]);
      }
    }
  }
}

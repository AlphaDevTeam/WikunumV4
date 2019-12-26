import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentHistory } from 'app/shared/model/document-history.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { DocumentHistoryService } from './document-history.service';
import { DocumentHistoryDeleteDialogComponent } from './document-history-delete-dialog.component';

@Component({
  selector: 'jhi-document-history',
  templateUrl: './document-history.component.html'
})
export class DocumentHistoryComponent implements OnInit, OnDestroy {
  documentHistories: IDocumentHistory[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected documentHistoryService: DocumentHistoryService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.documentHistories = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.documentHistoryService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IDocumentHistory[]>) => this.paginateDocumentHistories(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.documentHistories = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDocumentHistories();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDocumentHistory): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInDocumentHistories(): void {
    this.eventSubscriber = this.eventManager.subscribe('documentHistoryListModification', () => this.reset());
  }

  delete(documentHistory: IDocumentHistory): void {
    const modalRef = this.modalService.open(DocumentHistoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentHistory = documentHistory;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateDocumentHistories(data: IDocumentHistory[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.documentHistories.push(data[i]);
      }
    }
  }
}

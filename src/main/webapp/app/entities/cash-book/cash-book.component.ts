import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICashBook } from 'app/shared/model/cash-book.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CashBookService } from './cash-book.service';
import { CashBookDeleteDialogComponent } from './cash-book-delete-dialog.component';

@Component({
  selector: 'jhi-cash-book',
  templateUrl: './cash-book.component.html'
})
export class CashBookComponent implements OnInit, OnDestroy {
  cashBooks: ICashBook[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected cashBookService: CashBookService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.cashBooks = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.cashBookService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ICashBook[]>) => this.paginateCashBooks(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.cashBooks = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCashBooks();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICashBook): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCashBooks(): void {
    this.eventSubscriber = this.eventManager.subscribe('cashBookListModification', () => this.reset());
  }

  delete(cashBook: ICashBook): void {
    const modalRef = this.modalService.open(CashBookDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cashBook = cashBook;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCashBooks(data: ICashBook[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.cashBooks.push(data[i]);
      }
    }
  }
}

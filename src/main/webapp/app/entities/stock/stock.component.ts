import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStock } from 'app/shared/model/stock.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { StockService } from './stock.service';
import { StockDeleteDialogComponent } from './stock-delete-dialog.component';

@Component({
  selector: 'jhi-stock',
  templateUrl: './stock.component.html'
})
export class StockComponent implements OnInit, OnDestroy {
  stocks: IStock[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected stockService: StockService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.stocks = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.stockService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IStock[]>) => this.paginateStocks(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.stocks = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInStocks();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IStock): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInStocks(): void {
    this.eventSubscriber = this.eventManager.subscribe('stockListModification', () => this.reset());
  }

  delete(stock: IStock): void {
    const modalRef = this.modalService.open(StockDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.stock = stock;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateStocks(data: IStock[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.stocks.push(data[i]);
      }
    }
  }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICurrency } from 'app/shared/model/currency.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CurrencyService } from './currency.service';
import { CurrencyDeleteDialogComponent } from './currency-delete-dialog.component';

@Component({
  selector: 'jhi-currency',
  templateUrl: './currency.component.html'
})
export class CurrencyComponent implements OnInit, OnDestroy {
  currencies: ICurrency[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected currencyService: CurrencyService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.currencies = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.currencyService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ICurrency[]>) => this.paginateCurrencies(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.currencies = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCurrencies();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICurrency): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCurrencies(): void {
    this.eventSubscriber = this.eventManager.subscribe('currencyListModification', () => this.reset());
  }

  delete(currency: ICurrency): void {
    const modalRef = this.modalService.open(CurrencyDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.currency = currency;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCurrencies(data: ICurrency[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.currencies.push(data[i]);
      }
    }
  }
}

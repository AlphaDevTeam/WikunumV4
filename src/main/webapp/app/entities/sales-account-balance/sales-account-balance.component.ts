import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISalesAccountBalance } from 'app/shared/model/sales-account-balance.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { SalesAccountBalanceService } from './sales-account-balance.service';
import { SalesAccountBalanceDeleteDialogComponent } from './sales-account-balance-delete-dialog.component';

@Component({
  selector: 'jhi-sales-account-balance',
  templateUrl: './sales-account-balance.component.html'
})
export class SalesAccountBalanceComponent implements OnInit, OnDestroy {
  salesAccountBalances: ISalesAccountBalance[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected salesAccountBalanceService: SalesAccountBalanceService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.salesAccountBalances = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.salesAccountBalanceService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ISalesAccountBalance[]>) => this.paginateSalesAccountBalances(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.salesAccountBalances = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSalesAccountBalances();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISalesAccountBalance): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSalesAccountBalances(): void {
    this.eventSubscriber = this.eventManager.subscribe('salesAccountBalanceListModification', () => this.reset());
  }

  delete(salesAccountBalance: ISalesAccountBalance): void {
    const modalRef = this.modalService.open(SalesAccountBalanceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.salesAccountBalance = salesAccountBalance;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateSalesAccountBalances(data: ISalesAccountBalance[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.salesAccountBalances.push(data[i]);
      }
    }
  }
}

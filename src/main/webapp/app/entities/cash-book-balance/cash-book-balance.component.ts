import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICashBookBalance } from 'app/shared/model/cash-book-balance.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CashBookBalanceService } from './cash-book-balance.service';
import { CashBookBalanceDeleteDialogComponent } from './cash-book-balance-delete-dialog.component';

@Component({
  selector: 'jhi-cash-book-balance',
  templateUrl: './cash-book-balance.component.html'
})
export class CashBookBalanceComponent implements OnInit, OnDestroy {
  cashBookBalances: ICashBookBalance[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected cashBookBalanceService: CashBookBalanceService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.cashBookBalances = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.cashBookBalanceService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ICashBookBalance[]>) => this.paginateCashBookBalances(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.cashBookBalances = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCashBookBalances();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICashBookBalance): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCashBookBalances(): void {
    this.eventSubscriber = this.eventManager.subscribe('cashBookBalanceListModification', () => this.reset());
  }

  delete(cashBookBalance: ICashBookBalance): void {
    const modalRef = this.modalService.open(CashBookBalanceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cashBookBalance = cashBookBalance;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCashBookBalances(data: ICashBookBalance[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.cashBookBalances.push(data[i]);
      }
    }
  }
}

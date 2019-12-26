import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICashPaymentVoucherExpense } from 'app/shared/model/cash-payment-voucher-expense.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CashPaymentVoucherExpenseService } from './cash-payment-voucher-expense.service';
import { CashPaymentVoucherExpenseDeleteDialogComponent } from './cash-payment-voucher-expense-delete-dialog.component';

@Component({
  selector: 'jhi-cash-payment-voucher-expense',
  templateUrl: './cash-payment-voucher-expense.component.html'
})
export class CashPaymentVoucherExpenseComponent implements OnInit, OnDestroy {
  cashPaymentVoucherExpenses: ICashPaymentVoucherExpense[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected cashPaymentVoucherExpenseService: CashPaymentVoucherExpenseService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.cashPaymentVoucherExpenses = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.cashPaymentVoucherExpenseService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ICashPaymentVoucherExpense[]>) => this.paginateCashPaymentVoucherExpenses(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.cashPaymentVoucherExpenses = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCashPaymentVoucherExpenses();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICashPaymentVoucherExpense): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCashPaymentVoucherExpenses(): void {
    this.eventSubscriber = this.eventManager.subscribe('cashPaymentVoucherExpenseListModification', () => this.reset());
  }

  delete(cashPaymentVoucherExpense: ICashPaymentVoucherExpense): void {
    const modalRef = this.modalService.open(CashPaymentVoucherExpenseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cashPaymentVoucherExpense = cashPaymentVoucherExpense;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCashPaymentVoucherExpenses(data: ICashPaymentVoucherExpense[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.cashPaymentVoucherExpenses.push(data[i]);
      }
    }
  }
}

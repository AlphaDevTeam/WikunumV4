import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICashReceiptVoucherExpense } from 'app/shared/model/cash-receipt-voucher-expense.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CashReceiptVoucherExpenseService } from './cash-receipt-voucher-expense.service';
import { CashReceiptVoucherExpenseDeleteDialogComponent } from './cash-receipt-voucher-expense-delete-dialog.component';

@Component({
  selector: 'jhi-cash-receipt-voucher-expense',
  templateUrl: './cash-receipt-voucher-expense.component.html'
})
export class CashReceiptVoucherExpenseComponent implements OnInit, OnDestroy {
  cashReceiptVoucherExpenses: ICashReceiptVoucherExpense[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected cashReceiptVoucherExpenseService: CashReceiptVoucherExpenseService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.cashReceiptVoucherExpenses = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.cashReceiptVoucherExpenseService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ICashReceiptVoucherExpense[]>) => this.paginateCashReceiptVoucherExpenses(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.cashReceiptVoucherExpenses = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCashReceiptVoucherExpenses();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICashReceiptVoucherExpense): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCashReceiptVoucherExpenses(): void {
    this.eventSubscriber = this.eventManager.subscribe('cashReceiptVoucherExpenseListModification', () => this.reset());
  }

  delete(cashReceiptVoucherExpense: ICashReceiptVoucherExpense): void {
    const modalRef = this.modalService.open(CashReceiptVoucherExpenseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cashReceiptVoucherExpense = cashReceiptVoucherExpense;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCashReceiptVoucherExpenses(data: ICashReceiptVoucherExpense[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.cashReceiptVoucherExpenses.push(data[i]);
      }
    }
  }
}

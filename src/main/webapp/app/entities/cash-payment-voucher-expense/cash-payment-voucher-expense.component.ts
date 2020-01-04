import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
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
  cashPaymentVoucherExpenses?: ICashPaymentVoucherExpense[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected cashPaymentVoucherExpenseService: CashPaymentVoucherExpenseService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page ? page : this.page;
    this.cashPaymentVoucherExpenseService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<ICashPaymentVoucherExpense[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.ascending = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
      this.ngbPaginationPage = data.pagingParams.page;
      this.loadPage();
    });
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
    this.eventSubscriber = this.eventManager.subscribe('cashPaymentVoucherExpenseListModification', () => this.loadPage());
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

  protected onSuccess(data: ICashPaymentVoucherExpense[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/cash-payment-voucher-expense'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.cashPaymentVoucherExpenses = data ? data : [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}

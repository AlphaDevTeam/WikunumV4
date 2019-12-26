import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICashReceiptVoucherCustomer } from 'app/shared/model/cash-receipt-voucher-customer.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CashReceiptVoucherCustomerService } from './cash-receipt-voucher-customer.service';
import { CashReceiptVoucherCustomerDeleteDialogComponent } from './cash-receipt-voucher-customer-delete-dialog.component';

@Component({
  selector: 'jhi-cash-receipt-voucher-customer',
  templateUrl: './cash-receipt-voucher-customer.component.html'
})
export class CashReceiptVoucherCustomerComponent implements OnInit, OnDestroy {
  cashReceiptVoucherCustomers: ICashReceiptVoucherCustomer[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected cashReceiptVoucherCustomerService: CashReceiptVoucherCustomerService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.cashReceiptVoucherCustomers = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.cashReceiptVoucherCustomerService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ICashReceiptVoucherCustomer[]>) => this.paginateCashReceiptVoucherCustomers(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.cashReceiptVoucherCustomers = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCashReceiptVoucherCustomers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICashReceiptVoucherCustomer): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCashReceiptVoucherCustomers(): void {
    this.eventSubscriber = this.eventManager.subscribe('cashReceiptVoucherCustomerListModification', () => this.reset());
  }

  delete(cashReceiptVoucherCustomer: ICashReceiptVoucherCustomer): void {
    const modalRef = this.modalService.open(CashReceiptVoucherCustomerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cashReceiptVoucherCustomer = cashReceiptVoucherCustomer;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCashReceiptVoucherCustomers(data: ICashReceiptVoucherCustomer[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.cashReceiptVoucherCustomers.push(data[i]);
      }
    }
  }
}

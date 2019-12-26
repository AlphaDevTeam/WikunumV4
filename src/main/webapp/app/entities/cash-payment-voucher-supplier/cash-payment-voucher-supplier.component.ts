import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICashPaymentVoucherSupplier } from 'app/shared/model/cash-payment-voucher-supplier.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CashPaymentVoucherSupplierService } from './cash-payment-voucher-supplier.service';
import { CashPaymentVoucherSupplierDeleteDialogComponent } from './cash-payment-voucher-supplier-delete-dialog.component';

@Component({
  selector: 'jhi-cash-payment-voucher-supplier',
  templateUrl: './cash-payment-voucher-supplier.component.html'
})
export class CashPaymentVoucherSupplierComponent implements OnInit, OnDestroy {
  cashPaymentVoucherSuppliers: ICashPaymentVoucherSupplier[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected cashPaymentVoucherSupplierService: CashPaymentVoucherSupplierService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.cashPaymentVoucherSuppliers = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.cashPaymentVoucherSupplierService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ICashPaymentVoucherSupplier[]>) => this.paginateCashPaymentVoucherSuppliers(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.cashPaymentVoucherSuppliers = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCashPaymentVoucherSuppliers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICashPaymentVoucherSupplier): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCashPaymentVoucherSuppliers(): void {
    this.eventSubscriber = this.eventManager.subscribe('cashPaymentVoucherSupplierListModification', () => this.reset());
  }

  delete(cashPaymentVoucherSupplier: ICashPaymentVoucherSupplier): void {
    const modalRef = this.modalService.open(CashPaymentVoucherSupplierDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cashPaymentVoucherSupplier = cashPaymentVoucherSupplier;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCashPaymentVoucherSuppliers(data: ICashPaymentVoucherSupplier[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.cashPaymentVoucherSuppliers.push(data[i]);
      }
    }
  }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaymentTypeBalance } from 'app/shared/model/payment-type-balance.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PaymentTypeBalanceService } from './payment-type-balance.service';
import { PaymentTypeBalanceDeleteDialogComponent } from './payment-type-balance-delete-dialog.component';

@Component({
  selector: 'jhi-payment-type-balance',
  templateUrl: './payment-type-balance.component.html'
})
export class PaymentTypeBalanceComponent implements OnInit, OnDestroy {
  paymentTypeBalances: IPaymentTypeBalance[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected paymentTypeBalanceService: PaymentTypeBalanceService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.paymentTypeBalances = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.paymentTypeBalanceService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IPaymentTypeBalance[]>) => this.paginatePaymentTypeBalances(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.paymentTypeBalances = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPaymentTypeBalances();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPaymentTypeBalance): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPaymentTypeBalances(): void {
    this.eventSubscriber = this.eventManager.subscribe('paymentTypeBalanceListModification', () => this.reset());
  }

  delete(paymentTypeBalance: IPaymentTypeBalance): void {
    const modalRef = this.modalService.open(PaymentTypeBalanceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.paymentTypeBalance = paymentTypeBalance;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePaymentTypeBalances(data: IPaymentTypeBalance[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.paymentTypeBalances.push(data[i]);
      }
    }
  }
}

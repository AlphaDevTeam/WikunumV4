import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaymentTypeAccount } from 'app/shared/model/payment-type-account.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PaymentTypeAccountService } from './payment-type-account.service';
import { PaymentTypeAccountDeleteDialogComponent } from './payment-type-account-delete-dialog.component';

@Component({
  selector: 'jhi-payment-type-account',
  templateUrl: './payment-type-account.component.html'
})
export class PaymentTypeAccountComponent implements OnInit, OnDestroy {
  paymentTypeAccounts: IPaymentTypeAccount[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected paymentTypeAccountService: PaymentTypeAccountService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.paymentTypeAccounts = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.paymentTypeAccountService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IPaymentTypeAccount[]>) => this.paginatePaymentTypeAccounts(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.paymentTypeAccounts = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPaymentTypeAccounts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPaymentTypeAccount): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPaymentTypeAccounts(): void {
    this.eventSubscriber = this.eventManager.subscribe('paymentTypeAccountListModification', () => this.reset());
  }

  delete(paymentTypeAccount: IPaymentTypeAccount): void {
    const modalRef = this.modalService.open(PaymentTypeAccountDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.paymentTypeAccount = paymentTypeAccount;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePaymentTypeAccounts(data: IPaymentTypeAccount[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.paymentTypeAccounts.push(data[i]);
      }
    }
  }
}

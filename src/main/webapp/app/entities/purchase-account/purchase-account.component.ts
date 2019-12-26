import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPurchaseAccount } from 'app/shared/model/purchase-account.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PurchaseAccountService } from './purchase-account.service';
import { PurchaseAccountDeleteDialogComponent } from './purchase-account-delete-dialog.component';

@Component({
  selector: 'jhi-purchase-account',
  templateUrl: './purchase-account.component.html'
})
export class PurchaseAccountComponent implements OnInit, OnDestroy {
  purchaseAccounts: IPurchaseAccount[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected purchaseAccountService: PurchaseAccountService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.purchaseAccounts = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.purchaseAccountService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IPurchaseAccount[]>) => this.paginatePurchaseAccounts(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.purchaseAccounts = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPurchaseAccounts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPurchaseAccount): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPurchaseAccounts(): void {
    this.eventSubscriber = this.eventManager.subscribe('purchaseAccountListModification', () => this.reset());
  }

  delete(purchaseAccount: IPurchaseAccount): void {
    const modalRef = this.modalService.open(PurchaseAccountDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.purchaseAccount = purchaseAccount;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePurchaseAccounts(data: IPurchaseAccount[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.purchaseAccounts.push(data[i]);
      }
    }
  }
}

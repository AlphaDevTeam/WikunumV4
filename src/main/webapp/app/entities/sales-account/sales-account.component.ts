import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISalesAccount } from 'app/shared/model/sales-account.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { SalesAccountService } from './sales-account.service';
import { SalesAccountDeleteDialogComponent } from './sales-account-delete-dialog.component';

@Component({
  selector: 'jhi-sales-account',
  templateUrl: './sales-account.component.html'
})
export class SalesAccountComponent implements OnInit, OnDestroy {
  salesAccounts: ISalesAccount[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected salesAccountService: SalesAccountService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.salesAccounts = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.salesAccountService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ISalesAccount[]>) => this.paginateSalesAccounts(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.salesAccounts = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSalesAccounts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISalesAccount): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSalesAccounts(): void {
    this.eventSubscriber = this.eventManager.subscribe('salesAccountListModification', () => this.reset());
  }

  delete(salesAccount: ISalesAccount): void {
    const modalRef = this.modalService.open(SalesAccountDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.salesAccount = salesAccount;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateSalesAccounts(data: ISalesAccount[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.salesAccounts.push(data[i]);
      }
    }
  }
}

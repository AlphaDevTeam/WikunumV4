import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICustomerAccount } from 'app/shared/model/customer-account.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CustomerAccountService } from './customer-account.service';
import { CustomerAccountDeleteDialogComponent } from './customer-account-delete-dialog.component';

@Component({
  selector: 'jhi-customer-account',
  templateUrl: './customer-account.component.html'
})
export class CustomerAccountComponent implements OnInit, OnDestroy {
  customerAccounts: ICustomerAccount[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected customerAccountService: CustomerAccountService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.customerAccounts = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.customerAccountService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ICustomerAccount[]>) => this.paginateCustomerAccounts(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.customerAccounts = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCustomerAccounts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICustomerAccount): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCustomerAccounts(): void {
    this.eventSubscriber = this.eventManager.subscribe('customerAccountListModification', () => this.reset());
  }

  delete(customerAccount: ICustomerAccount): void {
    const modalRef = this.modalService.open(CustomerAccountDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.customerAccount = customerAccount;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCustomerAccounts(data: ICustomerAccount[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.customerAccounts.push(data[i]);
      }
    }
  }
}

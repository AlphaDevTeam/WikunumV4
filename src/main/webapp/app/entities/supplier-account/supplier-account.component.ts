import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISupplierAccount } from 'app/shared/model/supplier-account.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { SupplierAccountService } from './supplier-account.service';
import { SupplierAccountDeleteDialogComponent } from './supplier-account-delete-dialog.component';

@Component({
  selector: 'jhi-supplier-account',
  templateUrl: './supplier-account.component.html'
})
export class SupplierAccountComponent implements OnInit, OnDestroy {
  supplierAccounts: ISupplierAccount[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected supplierAccountService: SupplierAccountService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.supplierAccounts = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.supplierAccountService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ISupplierAccount[]>) => this.paginateSupplierAccounts(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.supplierAccounts = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSupplierAccounts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISupplierAccount): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSupplierAccounts(): void {
    this.eventSubscriber = this.eventManager.subscribe('supplierAccountListModification', () => this.reset());
  }

  delete(supplierAccount: ISupplierAccount): void {
    const modalRef = this.modalService.open(SupplierAccountDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.supplierAccount = supplierAccount;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateSupplierAccounts(data: ISupplierAccount[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.supplierAccounts.push(data[i]);
      }
    }
  }
}

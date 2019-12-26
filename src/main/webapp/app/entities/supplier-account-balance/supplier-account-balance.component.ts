import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISupplierAccountBalance } from 'app/shared/model/supplier-account-balance.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { SupplierAccountBalanceService } from './supplier-account-balance.service';
import { SupplierAccountBalanceDeleteDialogComponent } from './supplier-account-balance-delete-dialog.component';

@Component({
  selector: 'jhi-supplier-account-balance',
  templateUrl: './supplier-account-balance.component.html'
})
export class SupplierAccountBalanceComponent implements OnInit, OnDestroy {
  supplierAccountBalances: ISupplierAccountBalance[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected supplierAccountBalanceService: SupplierAccountBalanceService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.supplierAccountBalances = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.supplierAccountBalanceService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ISupplierAccountBalance[]>) => this.paginateSupplierAccountBalances(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.supplierAccountBalances = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSupplierAccountBalances();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISupplierAccountBalance): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSupplierAccountBalances(): void {
    this.eventSubscriber = this.eventManager.subscribe('supplierAccountBalanceListModification', () => this.reset());
  }

  delete(supplierAccountBalance: ISupplierAccountBalance): void {
    const modalRef = this.modalService.open(SupplierAccountBalanceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.supplierAccountBalance = supplierAccountBalance;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateSupplierAccountBalances(data: ISupplierAccountBalance[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.supplierAccountBalances.push(data[i]);
      }
    }
  }
}

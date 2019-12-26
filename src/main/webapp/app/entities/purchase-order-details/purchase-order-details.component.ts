import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPurchaseOrderDetails } from 'app/shared/model/purchase-order-details.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PurchaseOrderDetailsService } from './purchase-order-details.service';
import { PurchaseOrderDetailsDeleteDialogComponent } from './purchase-order-details-delete-dialog.component';

@Component({
  selector: 'jhi-purchase-order-details',
  templateUrl: './purchase-order-details.component.html'
})
export class PurchaseOrderDetailsComponent implements OnInit, OnDestroy {
  purchaseOrderDetails: IPurchaseOrderDetails[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected purchaseOrderDetailsService: PurchaseOrderDetailsService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.purchaseOrderDetails = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.purchaseOrderDetailsService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IPurchaseOrderDetails[]>) => this.paginatePurchaseOrderDetails(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.purchaseOrderDetails = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPurchaseOrderDetails();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPurchaseOrderDetails): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPurchaseOrderDetails(): void {
    this.eventSubscriber = this.eventManager.subscribe('purchaseOrderDetailsListModification', () => this.reset());
  }

  delete(purchaseOrderDetails: IPurchaseOrderDetails): void {
    const modalRef = this.modalService.open(PurchaseOrderDetailsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.purchaseOrderDetails = purchaseOrderDetails;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePurchaseOrderDetails(data: IPurchaseOrderDetails[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.purchaseOrderDetails.push(data[i]);
      }
    }
  }
}

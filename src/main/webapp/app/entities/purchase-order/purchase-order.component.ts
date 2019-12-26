import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPurchaseOrder } from 'app/shared/model/purchase-order.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PurchaseOrderService } from './purchase-order.service';
import { PurchaseOrderDeleteDialogComponent } from './purchase-order-delete-dialog.component';

@Component({
  selector: 'jhi-purchase-order',
  templateUrl: './purchase-order.component.html'
})
export class PurchaseOrderComponent implements OnInit, OnDestroy {
  purchaseOrders: IPurchaseOrder[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected purchaseOrderService: PurchaseOrderService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.purchaseOrders = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.purchaseOrderService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IPurchaseOrder[]>) => this.paginatePurchaseOrders(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.purchaseOrders = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPurchaseOrders();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPurchaseOrder): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPurchaseOrders(): void {
    this.eventSubscriber = this.eventManager.subscribe('purchaseOrderListModification', () => this.reset());
  }

  delete(purchaseOrder: IPurchaseOrder): void {
    const modalRef = this.modalService.open(PurchaseOrderDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.purchaseOrder = purchaseOrder;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePurchaseOrders(data: IPurchaseOrder[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.purchaseOrders.push(data[i]);
      }
    }
  }
}

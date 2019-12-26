import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGoodsReceipt } from 'app/shared/model/goods-receipt.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { GoodsReceiptService } from './goods-receipt.service';
import { GoodsReceiptDeleteDialogComponent } from './goods-receipt-delete-dialog.component';

@Component({
  selector: 'jhi-goods-receipt',
  templateUrl: './goods-receipt.component.html'
})
export class GoodsReceiptComponent implements OnInit, OnDestroy {
  goodsReceipts: IGoodsReceipt[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected goodsReceiptService: GoodsReceiptService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.goodsReceipts = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.goodsReceiptService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IGoodsReceipt[]>) => this.paginateGoodsReceipts(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.goodsReceipts = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInGoodsReceipts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IGoodsReceipt): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInGoodsReceipts(): void {
    this.eventSubscriber = this.eventManager.subscribe('goodsReceiptListModification', () => this.reset());
  }

  delete(goodsReceipt: IGoodsReceipt): void {
    const modalRef = this.modalService.open(GoodsReceiptDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.goodsReceipt = goodsReceipt;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateGoodsReceipts(data: IGoodsReceipt[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.goodsReceipts.push(data[i]);
      }
    }
  }
}

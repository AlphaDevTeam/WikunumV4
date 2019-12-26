import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGoodsReceiptDetails } from 'app/shared/model/goods-receipt-details.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { GoodsReceiptDetailsService } from './goods-receipt-details.service';
import { GoodsReceiptDetailsDeleteDialogComponent } from './goods-receipt-details-delete-dialog.component';

@Component({
  selector: 'jhi-goods-receipt-details',
  templateUrl: './goods-receipt-details.component.html'
})
export class GoodsReceiptDetailsComponent implements OnInit, OnDestroy {
  goodsReceiptDetails: IGoodsReceiptDetails[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected goodsReceiptDetailsService: GoodsReceiptDetailsService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.goodsReceiptDetails = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.goodsReceiptDetailsService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IGoodsReceiptDetails[]>) => this.paginateGoodsReceiptDetails(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.goodsReceiptDetails = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInGoodsReceiptDetails();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IGoodsReceiptDetails): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInGoodsReceiptDetails(): void {
    this.eventSubscriber = this.eventManager.subscribe('goodsReceiptDetailsListModification', () => this.reset());
  }

  delete(goodsReceiptDetails: IGoodsReceiptDetails): void {
    const modalRef = this.modalService.open(GoodsReceiptDetailsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.goodsReceiptDetails = goodsReceiptDetails;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateGoodsReceiptDetails(data: IGoodsReceiptDetails[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.goodsReceiptDetails.push(data[i]);
      }
    }
  }
}

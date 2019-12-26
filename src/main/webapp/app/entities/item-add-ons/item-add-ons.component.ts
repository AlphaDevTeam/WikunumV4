import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemAddOns } from 'app/shared/model/item-add-ons.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ItemAddOnsService } from './item-add-ons.service';
import { ItemAddOnsDeleteDialogComponent } from './item-add-ons-delete-dialog.component';

@Component({
  selector: 'jhi-item-add-ons',
  templateUrl: './item-add-ons.component.html'
})
export class ItemAddOnsComponent implements OnInit, OnDestroy {
  itemAddOns: IItemAddOns[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected itemAddOnsService: ItemAddOnsService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.itemAddOns = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.itemAddOnsService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IItemAddOns[]>) => this.paginateItemAddOns(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.itemAddOns = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInItemAddOns();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IItemAddOns): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInItemAddOns(): void {
    this.eventSubscriber = this.eventManager.subscribe('itemAddOnsListModification', () => this.reset());
  }

  delete(itemAddOns: IItemAddOns): void {
    const modalRef = this.modalService.open(ItemAddOnsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.itemAddOns = itemAddOns;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateItemAddOns(data: IItemAddOns[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.itemAddOns.push(data[i]);
      }
    }
  }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITransactionType } from 'app/shared/model/transaction-type.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TransactionTypeService } from './transaction-type.service';
import { TransactionTypeDeleteDialogComponent } from './transaction-type-delete-dialog.component';

@Component({
  selector: 'jhi-transaction-type',
  templateUrl: './transaction-type.component.html'
})
export class TransactionTypeComponent implements OnInit, OnDestroy {
  transactionTypes: ITransactionType[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected transactionTypeService: TransactionTypeService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.transactionTypes = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.transactionTypeService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ITransactionType[]>) => this.paginateTransactionTypes(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.transactionTypes = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTransactionTypes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITransactionType): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTransactionTypes(): void {
    this.eventSubscriber = this.eventManager.subscribe('transactionTypeListModification', () => this.reset());
  }

  delete(transactionType: ITransactionType): void {
    const modalRef = this.modalService.open(TransactionTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.transactionType = transactionType;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTransactionTypes(data: ITransactionType[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.transactionTypes.push(data[i]);
      }
    }
  }
}

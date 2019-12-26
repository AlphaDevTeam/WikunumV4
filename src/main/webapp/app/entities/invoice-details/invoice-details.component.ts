import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInvoiceDetails } from 'app/shared/model/invoice-details.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { InvoiceDetailsService } from './invoice-details.service';
import { InvoiceDetailsDeleteDialogComponent } from './invoice-details-delete-dialog.component';

@Component({
  selector: 'jhi-invoice-details',
  templateUrl: './invoice-details.component.html'
})
export class InvoiceDetailsComponent implements OnInit, OnDestroy {
  invoiceDetails: IInvoiceDetails[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected invoiceDetailsService: InvoiceDetailsService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.invoiceDetails = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.invoiceDetailsService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IInvoiceDetails[]>) => this.paginateInvoiceDetails(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.invoiceDetails = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInInvoiceDetails();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IInvoiceDetails): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInInvoiceDetails(): void {
    this.eventSubscriber = this.eventManager.subscribe('invoiceDetailsListModification', () => this.reset());
  }

  delete(invoiceDetails: IInvoiceDetails): void {
    const modalRef = this.modalService.open(InvoiceDetailsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.invoiceDetails = invoiceDetails;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateInvoiceDetails(data: IInvoiceDetails[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.invoiceDetails.push(data[i]);
      }
    }
  }
}

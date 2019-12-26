import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentType } from 'app/shared/model/document-type.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { DocumentTypeService } from './document-type.service';
import { DocumentTypeDeleteDialogComponent } from './document-type-delete-dialog.component';

@Component({
  selector: 'jhi-document-type',
  templateUrl: './document-type.component.html'
})
export class DocumentTypeComponent implements OnInit, OnDestroy {
  documentTypes: IDocumentType[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected documentTypeService: DocumentTypeService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.documentTypes = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.documentTypeService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IDocumentType[]>) => this.paginateDocumentTypes(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.documentTypes = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDocumentTypes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDocumentType): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInDocumentTypes(): void {
    this.eventSubscriber = this.eventManager.subscribe('documentTypeListModification', () => this.reset());
  }

  delete(documentType: IDocumentType): void {
    const modalRef = this.modalService.open(DocumentTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentType = documentType;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateDocumentTypes(data: IDocumentType[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.documentTypes.push(data[i]);
      }
    }
  }
}

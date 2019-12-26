import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUnitOfMeasure } from 'app/shared/model/unit-of-measure.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { UnitOfMeasureService } from './unit-of-measure.service';
import { UnitOfMeasureDeleteDialogComponent } from './unit-of-measure-delete-dialog.component';

@Component({
  selector: 'jhi-unit-of-measure',
  templateUrl: './unit-of-measure.component.html'
})
export class UnitOfMeasureComponent implements OnInit, OnDestroy {
  unitOfMeasures: IUnitOfMeasure[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected unitOfMeasureService: UnitOfMeasureService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.unitOfMeasures = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.unitOfMeasureService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IUnitOfMeasure[]>) => this.paginateUnitOfMeasures(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.unitOfMeasures = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInUnitOfMeasures();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IUnitOfMeasure): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInUnitOfMeasures(): void {
    this.eventSubscriber = this.eventManager.subscribe('unitOfMeasureListModification', () => this.reset());
  }

  delete(unitOfMeasure: IUnitOfMeasure): void {
    const modalRef = this.modalService.open(UnitOfMeasureDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.unitOfMeasure = unitOfMeasure;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateUnitOfMeasures(data: IUnitOfMeasure[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.unitOfMeasures.push(data[i]);
      }
    }
  }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IConfigurationItems } from 'app/shared/model/configuration-items.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ConfigurationItemsService } from './configuration-items.service';
import { ConfigurationItemsDeleteDialogComponent } from './configuration-items-delete-dialog.component';

@Component({
  selector: 'jhi-configuration-items',
  templateUrl: './configuration-items.component.html'
})
export class ConfigurationItemsComponent implements OnInit, OnDestroy {
  configurationItems: IConfigurationItems[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected configurationItemsService: ConfigurationItemsService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.configurationItems = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.configurationItemsService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IConfigurationItems[]>) => this.paginateConfigurationItems(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.configurationItems = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInConfigurationItems();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IConfigurationItems): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInConfigurationItems(): void {
    this.eventSubscriber = this.eventManager.subscribe('configurationItemsListModification', () => this.reset());
  }

  delete(configurationItems: IConfigurationItems): void {
    const modalRef = this.modalService.open(ConfigurationItemsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.configurationItems = configurationItems;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateConfigurationItems(data: IConfigurationItems[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.configurationItems.push(data[i]);
      }
    }
  }
}

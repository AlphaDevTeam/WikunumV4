import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserPermissions } from 'app/shared/model/user-permissions.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { UserPermissionsService } from './user-permissions.service';
import { UserPermissionsDeleteDialogComponent } from './user-permissions-delete-dialog.component';

@Component({
  selector: 'jhi-user-permissions',
  templateUrl: './user-permissions.component.html'
})
export class UserPermissionsComponent implements OnInit, OnDestroy {
  userPermissions: IUserPermissions[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected userPermissionsService: UserPermissionsService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.userPermissions = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.userPermissionsService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IUserPermissions[]>) => this.paginateUserPermissions(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.userPermissions = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInUserPermissions();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IUserPermissions): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInUserPermissions(): void {
    this.eventSubscriber = this.eventManager.subscribe('userPermissionsListModification', () => this.reset());
  }

  delete(userPermissions: IUserPermissions): void {
    const modalRef = this.modalService.open(UserPermissionsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userPermissions = userPermissions;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateUserPermissions(data: IUserPermissions[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.userPermissions.push(data[i]);
      }
    }
  }
}

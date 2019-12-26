import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IExpenseAccount } from 'app/shared/model/expense-account.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ExpenseAccountService } from './expense-account.service';
import { ExpenseAccountDeleteDialogComponent } from './expense-account-delete-dialog.component';

@Component({
  selector: 'jhi-expense-account',
  templateUrl: './expense-account.component.html'
})
export class ExpenseAccountComponent implements OnInit, OnDestroy {
  expenseAccounts: IExpenseAccount[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected expenseAccountService: ExpenseAccountService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.expenseAccounts = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.expenseAccountService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IExpenseAccount[]>) => this.paginateExpenseAccounts(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.expenseAccounts = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInExpenseAccounts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IExpenseAccount): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInExpenseAccounts(): void {
    this.eventSubscriber = this.eventManager.subscribe('expenseAccountListModification', () => this.reset());
  }

  delete(expenseAccount: IExpenseAccount): void {
    const modalRef = this.modalService.open(ExpenseAccountDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.expenseAccount = expenseAccount;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateExpenseAccounts(data: IExpenseAccount[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.expenseAccounts.push(data[i]);
      }
    }
  }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IExpenseAccountBalance } from 'app/shared/model/expense-account-balance.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ExpenseAccountBalanceService } from './expense-account-balance.service';
import { ExpenseAccountBalanceDeleteDialogComponent } from './expense-account-balance-delete-dialog.component';

@Component({
  selector: 'jhi-expense-account-balance',
  templateUrl: './expense-account-balance.component.html'
})
export class ExpenseAccountBalanceComponent implements OnInit, OnDestroy {
  expenseAccountBalances: IExpenseAccountBalance[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected expenseAccountBalanceService: ExpenseAccountBalanceService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.expenseAccountBalances = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.expenseAccountBalanceService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IExpenseAccountBalance[]>) => this.paginateExpenseAccountBalances(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.expenseAccountBalances = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInExpenseAccountBalances();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IExpenseAccountBalance): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInExpenseAccountBalances(): void {
    this.eventSubscriber = this.eventManager.subscribe('expenseAccountBalanceListModification', () => this.reset());
  }

  delete(expenseAccountBalance: IExpenseAccountBalance): void {
    const modalRef = this.modalService.open(ExpenseAccountBalanceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.expenseAccountBalance = expenseAccountBalance;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateExpenseAccountBalances(data: IExpenseAccountBalance[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.expenseAccountBalances.push(data[i]);
      }
    }
  }
}

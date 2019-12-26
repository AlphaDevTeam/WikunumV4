import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IExpense } from 'app/shared/model/expense.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ExpenseService } from './expense.service';
import { ExpenseDeleteDialogComponent } from './expense-delete-dialog.component';

@Component({
  selector: 'jhi-expense',
  templateUrl: './expense.component.html'
})
export class ExpenseComponent implements OnInit, OnDestroy {
  expenses: IExpense[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected expenseService: ExpenseService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.expenses = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.expenseService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IExpense[]>) => this.paginateExpenses(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.expenses = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInExpenses();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IExpense): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInExpenses(): void {
    this.eventSubscriber = this.eventManager.subscribe('expenseListModification', () => this.reset());
  }

  delete(expense: IExpense): void {
    const modalRef = this.modalService.open(ExpenseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.expense = expense;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateExpenses(data: IExpense[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.expenses.push(data[i]);
      }
    }
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IExpenseAccountBalance, ExpenseAccountBalance } from 'app/shared/model/expense-account-balance.model';
import { ExpenseAccountBalanceService } from './expense-account-balance.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';
import { IExpense } from 'app/shared/model/expense.model';
import { ExpenseService } from 'app/entities/expense/expense.service';

type SelectableEntity = ILocation | ITransactionType | IExpense;

@Component({
  selector: 'jhi-expense-account-balance-update',
  templateUrl: './expense-account-balance-update.component.html'
})
export class ExpenseAccountBalanceUpdateComponent implements OnInit {
  isSaving = false;

  locations: ILocation[] = [];

  transactiontypes: ITransactionType[] = [];

  expenses: IExpense[] = [];

  editForm = this.fb.group({
    id: [],
    balance: [null, [Validators.required]],
    location: [null, Validators.required],
    transactionType: [null, Validators.required],
    expense: [null, Validators.required]
  });

  constructor(
    protected expenseAccountBalanceService: ExpenseAccountBalanceService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected expenseService: ExpenseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expenseAccountBalance }) => {
      this.updateForm(expenseAccountBalance);

      this.locationService
        .query()
        .pipe(
          map((res: HttpResponse<ILocation[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ILocation[]) => (this.locations = resBody));

      this.transactionTypeService
        .query()
        .pipe(
          map((res: HttpResponse<ITransactionType[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ITransactionType[]) => (this.transactiontypes = resBody));

      this.expenseService
        .query()
        .pipe(
          map((res: HttpResponse<IExpense[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IExpense[]) => (this.expenses = resBody));
    });
  }

  updateForm(expenseAccountBalance: IExpenseAccountBalance): void {
    this.editForm.patchValue({
      id: expenseAccountBalance.id,
      balance: expenseAccountBalance.balance,
      location: expenseAccountBalance.location,
      transactionType: expenseAccountBalance.transactionType,
      expense: expenseAccountBalance.expense
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expenseAccountBalance = this.createFromForm();
    if (expenseAccountBalance.id !== undefined) {
      this.subscribeToSaveResponse(this.expenseAccountBalanceService.update(expenseAccountBalance));
    } else {
      this.subscribeToSaveResponse(this.expenseAccountBalanceService.create(expenseAccountBalance));
    }
  }

  private createFromForm(): IExpenseAccountBalance {
    return {
      ...new ExpenseAccountBalance(),
      id: this.editForm.get(['id'])!.value,
      balance: this.editForm.get(['balance'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      expense: this.editForm.get(['expense'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpenseAccountBalance>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}

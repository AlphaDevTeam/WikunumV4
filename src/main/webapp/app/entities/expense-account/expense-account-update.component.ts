import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { IExpenseAccount, ExpenseAccount } from 'app/shared/model/expense-account.model';
import { ExpenseAccountService } from './expense-account.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';
import { IExpense } from 'app/shared/model/expense.model';
import { ExpenseService } from 'app/entities/expense/expense.service';

type SelectableEntity = ILocation | ITransactionType | IExpense;

@Component({
  selector: 'jhi-expense-account-update',
  templateUrl: './expense-account-update.component.html'
})
export class ExpenseAccountUpdateComponent implements OnInit {
  isSaving = false;

  locations: ILocation[] = [];

  transactiontypes: ITransactionType[] = [];

  expenses: IExpense[] = [];
  transactionDateDp: any;

  editForm = this.fb.group({
    id: [],
    transactionDate: [null, [Validators.required]],
    transactionDescription: [null, [Validators.required]],
    transactionAmountDR: [null, [Validators.required]],
    transactionAmountCR: [null, [Validators.required]],
    transactionBalance: [null, [Validators.required]],
    location: [null, Validators.required],
    transactionType: [null, Validators.required],
    expense: [null, Validators.required]
  });

  constructor(
    protected expenseAccountService: ExpenseAccountService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected expenseService: ExpenseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expenseAccount }) => {
      this.updateForm(expenseAccount);

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

  updateForm(expenseAccount: IExpenseAccount): void {
    this.editForm.patchValue({
      id: expenseAccount.id,
      transactionDate: expenseAccount.transactionDate,
      transactionDescription: expenseAccount.transactionDescription,
      transactionAmountDR: expenseAccount.transactionAmountDR,
      transactionAmountCR: expenseAccount.transactionAmountCR,
      transactionBalance: expenseAccount.transactionBalance,
      location: expenseAccount.location,
      transactionType: expenseAccount.transactionType,
      expense: expenseAccount.expense
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expenseAccount = this.createFromForm();
    if (expenseAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.expenseAccountService.update(expenseAccount));
    } else {
      this.subscribeToSaveResponse(this.expenseAccountService.create(expenseAccount));
    }
  }

  private createFromForm(): IExpenseAccount {
    return {
      ...new ExpenseAccount(),
      id: this.editForm.get(['id'])!.value,
      transactionDate: this.editForm.get(['transactionDate'])!.value,
      transactionDescription: this.editForm.get(['transactionDescription'])!.value,
      transactionAmountDR: this.editForm.get(['transactionAmountDR'])!.value,
      transactionAmountCR: this.editForm.get(['transactionAmountCR'])!.value,
      transactionBalance: this.editForm.get(['transactionBalance'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      expense: this.editForm.get(['expense'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpenseAccount>>): void {
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

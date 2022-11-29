import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { ICashPaymentVoucherExpense, CashPaymentVoucherExpense } from 'app/shared/model/cash-payment-voucher-expense.model';
import { CashPaymentVoucherExpenseService } from './cash-payment-voucher-expense.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';
import { IExpense } from 'app/shared/model/expense.model';
import { ExpenseService } from 'app/entities/expense/expense.service';

type SelectableEntity = ILocation | ITransactionType | IExpense;

@Component({
  selector: 'jhi-cash-payment-voucher-expense-update',
  templateUrl: './cash-payment-voucher-expense-update.component.html'
})
export class CashPaymentVoucherExpenseUpdateComponent implements OnInit {
  isSaving = false;

  locations: ILocation[] = [];

  transactiontypes: ITransactionType[] = [];

  expenses: IExpense[] = [];
  transactionDateDp: any;

  editForm = this.fb.group({
    id: [],
    transactionNumber: [null, [Validators.required]],
    transactionDate: [null, [Validators.required]],
    transactionDescription: [null, [Validators.required]],
    transactionAmount: [null, [Validators.required]],
    location: [null, Validators.required],
    transactionType: [null, Validators.required],
    expense: [null, Validators.required]
  });

  constructor(
    protected cashPaymentVoucherExpenseService: CashPaymentVoucherExpenseService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected expenseService: ExpenseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cashPaymentVoucherExpense }) => {
      this.updateForm(cashPaymentVoucherExpense);

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

  updateForm(cashPaymentVoucherExpense: ICashPaymentVoucherExpense): void {
    this.editForm.patchValue({
      id: cashPaymentVoucherExpense.id,
      transactionNumber: cashPaymentVoucherExpense.transactionNumber,
      transactionDate: cashPaymentVoucherExpense.transactionDate,
      transactionDescription: cashPaymentVoucherExpense.transactionDescription,
      transactionAmount: cashPaymentVoucherExpense.transactionAmount,
      location: cashPaymentVoucherExpense.location,
      transactionType: cashPaymentVoucherExpense.transactionType,
      expense: cashPaymentVoucherExpense.expense
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cashPaymentVoucherExpense = this.createFromForm();
    if (cashPaymentVoucherExpense.id !== undefined) {
      this.subscribeToSaveResponse(this.cashPaymentVoucherExpenseService.update(cashPaymentVoucherExpense));
    } else {
      this.subscribeToSaveResponse(this.cashPaymentVoucherExpenseService.create(cashPaymentVoucherExpense));
    }
  }

  private createFromForm(): ICashPaymentVoucherExpense {
    return {
      ...new CashPaymentVoucherExpense(),
      id: this.editForm.get(['id'])!.value,
      transactionNumber: this.editForm.get(['transactionNumber'])!.value,
      transactionDate: this.editForm.get(['transactionDate'])!.value,
      transactionDescription: this.editForm.get(['transactionDescription'])!.value,
      transactionAmount: this.editForm.get(['transactionAmount'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      expense: this.editForm.get(['expense'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICashPaymentVoucherExpense>>): void {
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

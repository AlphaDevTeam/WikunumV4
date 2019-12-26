import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IPaymentTypeBalance, PaymentTypeBalance } from 'app/shared/model/payment-type-balance.model';
import { PaymentTypeBalanceService } from './payment-type-balance.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';
import { IPaymentTypes } from 'app/shared/model/payment-types.model';
import { PaymentTypesService } from 'app/entities/payment-types/payment-types.service';

type SelectableEntity = ILocation | ITransactionType | IPaymentTypes;

@Component({
  selector: 'jhi-payment-type-balance-update',
  templateUrl: './payment-type-balance-update.component.html'
})
export class PaymentTypeBalanceUpdateComponent implements OnInit {
  isSaving = false;

  locations: ILocation[] = [];

  transactiontypes: ITransactionType[] = [];

  paymenttypes: IPaymentTypes[] = [];

  editForm = this.fb.group({
    id: [],
    balance: [null, [Validators.required]],
    location: [null, Validators.required],
    transactionType: [null, Validators.required],
    payType: [null, Validators.required]
  });

  constructor(
    protected paymentTypeBalanceService: PaymentTypeBalanceService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected paymentTypesService: PaymentTypesService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentTypeBalance }) => {
      this.updateForm(paymentTypeBalance);

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

      this.paymentTypesService
        .query()
        .pipe(
          map((res: HttpResponse<IPaymentTypes[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IPaymentTypes[]) => (this.paymenttypes = resBody));
    });
  }

  updateForm(paymentTypeBalance: IPaymentTypeBalance): void {
    this.editForm.patchValue({
      id: paymentTypeBalance.id,
      balance: paymentTypeBalance.balance,
      location: paymentTypeBalance.location,
      transactionType: paymentTypeBalance.transactionType,
      payType: paymentTypeBalance.payType
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paymentTypeBalance = this.createFromForm();
    if (paymentTypeBalance.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentTypeBalanceService.update(paymentTypeBalance));
    } else {
      this.subscribeToSaveResponse(this.paymentTypeBalanceService.create(paymentTypeBalance));
    }
  }

  private createFromForm(): IPaymentTypeBalance {
    return {
      ...new PaymentTypeBalance(),
      id: this.editForm.get(['id'])!.value,
      balance: this.editForm.get(['balance'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      payType: this.editForm.get(['payType'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentTypeBalance>>): void {
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

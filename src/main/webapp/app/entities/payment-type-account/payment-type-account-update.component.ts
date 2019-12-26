import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { IPaymentTypeAccount, PaymentTypeAccount } from 'app/shared/model/payment-type-account.model';
import { PaymentTypeAccountService } from './payment-type-account.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';
import { IPaymentTypes } from 'app/shared/model/payment-types.model';
import { PaymentTypesService } from 'app/entities/payment-types/payment-types.service';

type SelectableEntity = ILocation | ITransactionType | IPaymentTypes;

@Component({
  selector: 'jhi-payment-type-account-update',
  templateUrl: './payment-type-account-update.component.html'
})
export class PaymentTypeAccountUpdateComponent implements OnInit {
  isSaving = false;

  locations: ILocation[] = [];

  transactiontypes: ITransactionType[] = [];

  paymenttypes: IPaymentTypes[] = [];
  transactionDateDp: any;

  editForm = this.fb.group({
    id: [],
    transactionDate: [null, [Validators.required]],
    transactionDescription: [null, [Validators.required]],
    transactionAmount: [null, [Validators.required]],
    transactionBalance: [null, [Validators.required]],
    location: [null, Validators.required],
    transactionType: [null, Validators.required],
    payType: [null, Validators.required]
  });

  constructor(
    protected paymentTypeAccountService: PaymentTypeAccountService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected paymentTypesService: PaymentTypesService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentTypeAccount }) => {
      this.updateForm(paymentTypeAccount);

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

  updateForm(paymentTypeAccount: IPaymentTypeAccount): void {
    this.editForm.patchValue({
      id: paymentTypeAccount.id,
      transactionDate: paymentTypeAccount.transactionDate,
      transactionDescription: paymentTypeAccount.transactionDescription,
      transactionAmount: paymentTypeAccount.transactionAmount,
      transactionBalance: paymentTypeAccount.transactionBalance,
      location: paymentTypeAccount.location,
      transactionType: paymentTypeAccount.transactionType,
      payType: paymentTypeAccount.payType
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paymentTypeAccount = this.createFromForm();
    if (paymentTypeAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentTypeAccountService.update(paymentTypeAccount));
    } else {
      this.subscribeToSaveResponse(this.paymentTypeAccountService.create(paymentTypeAccount));
    }
  }

  private createFromForm(): IPaymentTypeAccount {
    return {
      ...new PaymentTypeAccount(),
      id: this.editForm.get(['id'])!.value,
      transactionDate: this.editForm.get(['transactionDate'])!.value,
      transactionDescription: this.editForm.get(['transactionDescription'])!.value,
      transactionAmount: this.editForm.get(['transactionAmount'])!.value,
      transactionBalance: this.editForm.get(['transactionBalance'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      payType: this.editForm.get(['payType'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentTypeAccount>>): void {
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

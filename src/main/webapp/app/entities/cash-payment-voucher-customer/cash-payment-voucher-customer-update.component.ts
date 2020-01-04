import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { ICashPaymentVoucherCustomer, CashPaymentVoucherCustomer } from 'app/shared/model/cash-payment-voucher-customer.model';
import { CashPaymentVoucherCustomerService } from './cash-payment-voucher-customer.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer/customer.service';

type SelectableEntity = ILocation | ITransactionType | ICustomer;

@Component({
  selector: 'jhi-cash-payment-voucher-customer-update',
  templateUrl: './cash-payment-voucher-customer-update.component.html'
})
export class CashPaymentVoucherCustomerUpdateComponent implements OnInit {
  isSaving = false;

  locations: ILocation[] = [];

  transactiontypes: ITransactionType[] = [];

  customers: ICustomer[] = [];
  transactionDateDp: any;

  editForm = this.fb.group({
    id: [],
    transactionNumber: [null, [Validators.required]],
    transactionDate: [null, [Validators.required]],
    transactionDescription: [null, [Validators.required]],
    transactionAmount: [null, [Validators.required]],
    location: [null, Validators.required],
    transactionType: [null, Validators.required],
    customer: [null, Validators.required]
  });

  constructor(
    protected cashPaymentVoucherCustomerService: CashPaymentVoucherCustomerService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cashPaymentVoucherCustomer }) => {
      this.updateForm(cashPaymentVoucherCustomer);

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

      this.customerService
        .query()
        .pipe(
          map((res: HttpResponse<ICustomer[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ICustomer[]) => (this.customers = resBody));
    });
  }

  updateForm(cashPaymentVoucherCustomer: ICashPaymentVoucherCustomer): void {
    this.editForm.patchValue({
      id: cashPaymentVoucherCustomer.id,
      transactionNumber: cashPaymentVoucherCustomer.transactionNumber,
      transactionDate: cashPaymentVoucherCustomer.transactionDate,
      transactionDescription: cashPaymentVoucherCustomer.transactionDescription,
      transactionAmount: cashPaymentVoucherCustomer.transactionAmount,
      location: cashPaymentVoucherCustomer.location,
      transactionType: cashPaymentVoucherCustomer.transactionType,
      customer: cashPaymentVoucherCustomer.customer
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cashPaymentVoucherCustomer = this.createFromForm();
    if (cashPaymentVoucherCustomer.id !== undefined) {
      this.subscribeToSaveResponse(this.cashPaymentVoucherCustomerService.update(cashPaymentVoucherCustomer));
    } else {
      this.subscribeToSaveResponse(this.cashPaymentVoucherCustomerService.create(cashPaymentVoucherCustomer));
    }
  }

  private createFromForm(): ICashPaymentVoucherCustomer {
    return {
      ...new CashPaymentVoucherCustomer(),
      id: this.editForm.get(['id'])!.value,
      transactionNumber: this.editForm.get(['transactionNumber'])!.value,
      transactionDate: this.editForm.get(['transactionDate'])!.value,
      transactionDescription: this.editForm.get(['transactionDescription'])!.value,
      transactionAmount: this.editForm.get(['transactionAmount'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      customer: this.editForm.get(['customer'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICashPaymentVoucherCustomer>>): void {
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

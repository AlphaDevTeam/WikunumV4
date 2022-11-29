import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ICustomerAccountBalance, CustomerAccountBalance } from 'app/shared/model/customer-account-balance.model';
import { CustomerAccountBalanceService } from './customer-account-balance.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer/customer.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';

type SelectableEntity = ICustomer | ILocation | ITransactionType;

@Component({
  selector: 'jhi-customer-account-balance-update',
  templateUrl: './customer-account-balance-update.component.html'
})
export class CustomerAccountBalanceUpdateComponent implements OnInit {
  isSaving = false;

  customers: ICustomer[] = [];

  locations: ILocation[] = [];

  transactiontypes: ITransactionType[] = [];

  editForm = this.fb.group({
    id: [],
    balance: [null, [Validators.required]],
    customer: [null, Validators.required],
    location: [null, Validators.required],
    transactionType: [null, Validators.required]
  });

  constructor(
    protected customerAccountBalanceService: CustomerAccountBalanceService,
    protected customerService: CustomerService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerAccountBalance }) => {
      this.updateForm(customerAccountBalance);

      this.customerService
        .query()
        .pipe(
          map((res: HttpResponse<ICustomer[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ICustomer[]) => (this.customers = resBody));

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
    });
  }

  updateForm(customerAccountBalance: ICustomerAccountBalance): void {
    this.editForm.patchValue({
      id: customerAccountBalance.id,
      balance: customerAccountBalance.balance,
      customer: customerAccountBalance.customer,
      location: customerAccountBalance.location,
      transactionType: customerAccountBalance.transactionType
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customerAccountBalance = this.createFromForm();
    if (customerAccountBalance.id !== undefined) {
      this.subscribeToSaveResponse(this.customerAccountBalanceService.update(customerAccountBalance));
    } else {
      this.subscribeToSaveResponse(this.customerAccountBalanceService.create(customerAccountBalance));
    }
  }

  private createFromForm(): ICustomerAccountBalance {
    return {
      ...new CustomerAccountBalance(),
      id: this.editForm.get(['id'])!.value,
      balance: this.editForm.get(['balance'])!.value,
      customer: this.editForm.get(['customer'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerAccountBalance>>): void {
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

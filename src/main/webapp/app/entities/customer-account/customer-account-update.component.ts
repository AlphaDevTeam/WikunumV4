import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { ICustomerAccount, CustomerAccount } from 'app/shared/model/customer-account.model';
import { CustomerAccountService } from './customer-account.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer/customer.service';

type SelectableEntity = IDocumentHistory | ILocation | ITransactionType | ICustomer;

@Component({
  selector: 'jhi-customer-account-update',
  templateUrl: './customer-account-update.component.html'
})
export class CustomerAccountUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  locations: ILocation[] = [];

  transactiontypes: ITransactionType[] = [];

  customers: ICustomer[] = [];
  transactionDateDp: any;

  editForm = this.fb.group({
    id: [],
    transactionDate: [null, [Validators.required]],
    transactionDescription: [null, [Validators.required]],
    transactionAmountDR: [null, [Validators.required]],
    transactionAmountCR: [null, [Validators.required]],
    transactionBalance: [null, [Validators.required]],
    history: [],
    location: [null, Validators.required],
    transactionType: [null, Validators.required],
    customer: [null, Validators.required]
  });

  constructor(
    protected customerAccountService: CustomerAccountService,
    protected documentHistoryService: DocumentHistoryService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerAccount }) => {
      this.updateForm(customerAccount);

      this.documentHistoryService
        .query({ 'customerAccountId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!customerAccount.history || !customerAccount.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(customerAccount.history.id)
              .pipe(
                map((subRes: HttpResponse<IDocumentHistory>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IDocumentHistory[]) => {
                this.histories = concatRes;
              });
          }
        });

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

  updateForm(customerAccount: ICustomerAccount): void {
    this.editForm.patchValue({
      id: customerAccount.id,
      transactionDate: customerAccount.transactionDate,
      transactionDescription: customerAccount.transactionDescription,
      transactionAmountDR: customerAccount.transactionAmountDR,
      transactionAmountCR: customerAccount.transactionAmountCR,
      transactionBalance: customerAccount.transactionBalance,
      history: customerAccount.history,
      location: customerAccount.location,
      transactionType: customerAccount.transactionType,
      customer: customerAccount.customer
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customerAccount = this.createFromForm();
    if (customerAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.customerAccountService.update(customerAccount));
    } else {
      this.subscribeToSaveResponse(this.customerAccountService.create(customerAccount));
    }
  }

  private createFromForm(): ICustomerAccount {
    return {
      ...new CustomerAccount(),
      id: this.editForm.get(['id'])!.value,
      transactionDate: this.editForm.get(['transactionDate'])!.value,
      transactionDescription: this.editForm.get(['transactionDescription'])!.value,
      transactionAmountDR: this.editForm.get(['transactionAmountDR'])!.value,
      transactionAmountCR: this.editForm.get(['transactionAmountCR'])!.value,
      transactionBalance: this.editForm.get(['transactionBalance'])!.value,
      history: this.editForm.get(['history'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      customer: this.editForm.get(['customer'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerAccount>>): void {
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

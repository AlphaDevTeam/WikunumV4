import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { ICashReceiptVoucherCustomer, CashReceiptVoucherCustomer } from 'app/shared/model/cash-receipt-voucher-customer.model';
import { CashReceiptVoucherCustomerService } from './cash-receipt-voucher-customer.service';
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
  selector: 'jhi-cash-receipt-voucher-customer-update',
  templateUrl: './cash-receipt-voucher-customer-update.component.html'
})
export class CashReceiptVoucherCustomerUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

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
    history: [],
    location: [null, Validators.required],
    transactionType: [null, Validators.required],
    customer: [null, Validators.required]
  });

  constructor(
    protected cashReceiptVoucherCustomerService: CashReceiptVoucherCustomerService,
    protected documentHistoryService: DocumentHistoryService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cashReceiptVoucherCustomer }) => {
      this.updateForm(cashReceiptVoucherCustomer);

      this.documentHistoryService
        .query({ 'cashReceiptVoucherCustomerId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!cashReceiptVoucherCustomer.history || !cashReceiptVoucherCustomer.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(cashReceiptVoucherCustomer.history.id)
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

  updateForm(cashReceiptVoucherCustomer: ICashReceiptVoucherCustomer): void {
    this.editForm.patchValue({
      id: cashReceiptVoucherCustomer.id,
      transactionNumber: cashReceiptVoucherCustomer.transactionNumber,
      transactionDate: cashReceiptVoucherCustomer.transactionDate,
      transactionDescription: cashReceiptVoucherCustomer.transactionDescription,
      transactionAmount: cashReceiptVoucherCustomer.transactionAmount,
      history: cashReceiptVoucherCustomer.history,
      location: cashReceiptVoucherCustomer.location,
      transactionType: cashReceiptVoucherCustomer.transactionType,
      customer: cashReceiptVoucherCustomer.customer
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cashReceiptVoucherCustomer = this.createFromForm();
    if (cashReceiptVoucherCustomer.id !== undefined) {
      this.subscribeToSaveResponse(this.cashReceiptVoucherCustomerService.update(cashReceiptVoucherCustomer));
    } else {
      this.subscribeToSaveResponse(this.cashReceiptVoucherCustomerService.create(cashReceiptVoucherCustomer));
    }
  }

  private createFromForm(): ICashReceiptVoucherCustomer {
    return {
      ...new CashReceiptVoucherCustomer(),
      id: this.editForm.get(['id'])!.value,
      transactionNumber: this.editForm.get(['transactionNumber'])!.value,
      transactionDate: this.editForm.get(['transactionDate'])!.value,
      transactionDescription: this.editForm.get(['transactionDescription'])!.value,
      transactionAmount: this.editForm.get(['transactionAmount'])!.value,
      history: this.editForm.get(['history'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      customer: this.editForm.get(['customer'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICashReceiptVoucherCustomer>>): void {
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

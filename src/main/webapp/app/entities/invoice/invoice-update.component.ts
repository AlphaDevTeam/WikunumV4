import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { IInvoice, Invoice } from 'app/shared/model/invoice.model';
import { InvoiceService } from './invoice.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer/customer.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

type SelectableEntity = ICustomer | ITransactionType | ILocation;

@Component({
  selector: 'jhi-invoice-update',
  templateUrl: './invoice-update.component.html'
})
export class InvoiceUpdateComponent implements OnInit {
  isSaving = false;

  customers: ICustomer[] = [];

  transactiontypes: ITransactionType[] = [];

  locations: ILocation[] = [];
  invDateDp: any;

  editForm = this.fb.group({
    id: [],
    invNumber: [null, [Validators.required]],
    invDate: [null, [Validators.required]],
    invTotalAmount: [null, [Validators.required]],
    cashAmount: [],
    cardAmount: [],
    dueAmount: [],
    customer: [null, Validators.required],
    transactionType: [null, Validators.required],
    location: [null, Validators.required]
  });

  constructor(
    protected invoiceService: InvoiceService,
    protected customerService: CustomerService,
    protected transactionTypeService: TransactionTypeService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.updateForm(invoice);

      this.customerService
        .query()
        .pipe(
          map((res: HttpResponse<ICustomer[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ICustomer[]) => (this.customers = resBody));

      this.transactionTypeService
        .query()
        .pipe(
          map((res: HttpResponse<ITransactionType[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ITransactionType[]) => (this.transactiontypes = resBody));

      this.locationService
        .query()
        .pipe(
          map((res: HttpResponse<ILocation[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ILocation[]) => (this.locations = resBody));
    });
  }

  updateForm(invoice: IInvoice): void {
    this.editForm.patchValue({
      id: invoice.id,
      invNumber: invoice.invNumber,
      invDate: invoice.invDate,
      invTotalAmount: invoice.invTotalAmount,
      cashAmount: invoice.cashAmount,
      cardAmount: invoice.cardAmount,
      dueAmount: invoice.dueAmount,
      customer: invoice.customer,
      transactionType: invoice.transactionType,
      location: invoice.location
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoice = this.createFromForm();
    if (invoice.id !== undefined) {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    }
  }

  private createFromForm(): IInvoice {
    return {
      ...new Invoice(),
      id: this.editForm.get(['id'])!.value,
      invNumber: this.editForm.get(['invNumber'])!.value,
      invDate: this.editForm.get(['invDate'])!.value,
      invTotalAmount: this.editForm.get(['invTotalAmount'])!.value,
      cashAmount: this.editForm.get(['cashAmount'])!.value,
      cardAmount: this.editForm.get(['cardAmount'])!.value,
      dueAmount: this.editForm.get(['dueAmount'])!.value,
      customer: this.editForm.get(['customer'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      location: this.editForm.get(['location'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>): void {
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

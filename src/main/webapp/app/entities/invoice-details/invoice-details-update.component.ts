import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IInvoiceDetails, InvoiceDetails } from 'app/shared/model/invoice-details.model';
import { InvoiceDetailsService } from './invoice-details.service';
import { IItems } from 'app/shared/model/items.model';
import { ItemsService } from 'app/entities/items/items.service';
import { IInvoice } from 'app/shared/model/invoice.model';
import { InvoiceService } from 'app/entities/invoice/invoice.service';

type SelectableEntity = IItems | IInvoice;

@Component({
  selector: 'jhi-invoice-details-update',
  templateUrl: './invoice-details-update.component.html'
})
export class InvoiceDetailsUpdateComponent implements OnInit {
  isSaving = false;

  items: IItems[] = [];

  invoices: IInvoice[] = [];

  editForm = this.fb.group({
    id: [],
    invQty: [null, [Validators.required]],
    revisedItemSalesPrice: [],
    item: [null, Validators.required],
    inv: [null, Validators.required]
  });

  constructor(
    protected invoiceDetailsService: InvoiceDetailsService,
    protected itemsService: ItemsService,
    protected invoiceService: InvoiceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceDetails }) => {
      this.updateForm(invoiceDetails);

      this.itemsService
        .query()
        .pipe(
          map((res: HttpResponse<IItems[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IItems[]) => (this.items = resBody));

      this.invoiceService
        .query()
        .pipe(
          map((res: HttpResponse<IInvoice[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IInvoice[]) => (this.invoices = resBody));
    });
  }

  updateForm(invoiceDetails: IInvoiceDetails): void {
    this.editForm.patchValue({
      id: invoiceDetails.id,
      invQty: invoiceDetails.invQty,
      revisedItemSalesPrice: invoiceDetails.revisedItemSalesPrice,
      item: invoiceDetails.item,
      inv: invoiceDetails.inv
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoiceDetails = this.createFromForm();
    if (invoiceDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.invoiceDetailsService.update(invoiceDetails));
    } else {
      this.subscribeToSaveResponse(this.invoiceDetailsService.create(invoiceDetails));
    }
  }

  private createFromForm(): IInvoiceDetails {
    return {
      ...new InvoiceDetails(),
      id: this.editForm.get(['id'])!.value,
      invQty: this.editForm.get(['invQty'])!.value,
      revisedItemSalesPrice: this.editForm.get(['revisedItemSalesPrice'])!.value,
      item: this.editForm.get(['item'])!.value,
      inv: this.editForm.get(['inv'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoiceDetails>>): void {
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

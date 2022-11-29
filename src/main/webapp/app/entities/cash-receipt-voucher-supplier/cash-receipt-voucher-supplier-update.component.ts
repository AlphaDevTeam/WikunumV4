import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { ICashReceiptVoucherSupplier, CashReceiptVoucherSupplier } from 'app/shared/model/cash-receipt-voucher-supplier.model';
import { CashReceiptVoucherSupplierService } from './cash-receipt-voucher-supplier.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService } from 'app/entities/supplier/supplier.service';

type SelectableEntity = IDocumentHistory | ILocation | ITransactionType | ISupplier;

@Component({
  selector: 'jhi-cash-receipt-voucher-supplier-update',
  templateUrl: './cash-receipt-voucher-supplier-update.component.html'
})
export class CashReceiptVoucherSupplierUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  locations: ILocation[] = [];

  transactiontypes: ITransactionType[] = [];

  suppliers: ISupplier[] = [];
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
    supplier: [null, Validators.required]
  });

  constructor(
    protected cashReceiptVoucherSupplierService: CashReceiptVoucherSupplierService,
    protected documentHistoryService: DocumentHistoryService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected supplierService: SupplierService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cashReceiptVoucherSupplier }) => {
      this.updateForm(cashReceiptVoucherSupplier);

      this.documentHistoryService
        .query({ 'cashReceiptVoucherSupplierId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!cashReceiptVoucherSupplier.history || !cashReceiptVoucherSupplier.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(cashReceiptVoucherSupplier.history.id)
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

      this.supplierService
        .query()
        .pipe(
          map((res: HttpResponse<ISupplier[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ISupplier[]) => (this.suppliers = resBody));
    });
  }

  updateForm(cashReceiptVoucherSupplier: ICashReceiptVoucherSupplier): void {
    this.editForm.patchValue({
      id: cashReceiptVoucherSupplier.id,
      transactionNumber: cashReceiptVoucherSupplier.transactionNumber,
      transactionDate: cashReceiptVoucherSupplier.transactionDate,
      transactionDescription: cashReceiptVoucherSupplier.transactionDescription,
      transactionAmount: cashReceiptVoucherSupplier.transactionAmount,
      history: cashReceiptVoucherSupplier.history,
      location: cashReceiptVoucherSupplier.location,
      transactionType: cashReceiptVoucherSupplier.transactionType,
      supplier: cashReceiptVoucherSupplier.supplier
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cashReceiptVoucherSupplier = this.createFromForm();
    if (cashReceiptVoucherSupplier.id !== undefined) {
      this.subscribeToSaveResponse(this.cashReceiptVoucherSupplierService.update(cashReceiptVoucherSupplier));
    } else {
      this.subscribeToSaveResponse(this.cashReceiptVoucherSupplierService.create(cashReceiptVoucherSupplier));
    }
  }

  private createFromForm(): ICashReceiptVoucherSupplier {
    return {
      ...new CashReceiptVoucherSupplier(),
      id: this.editForm.get(['id'])!.value,
      transactionNumber: this.editForm.get(['transactionNumber'])!.value,
      transactionDate: this.editForm.get(['transactionDate'])!.value,
      transactionDescription: this.editForm.get(['transactionDescription'])!.value,
      transactionAmount: this.editForm.get(['transactionAmount'])!.value,
      history: this.editForm.get(['history'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      supplier: this.editForm.get(['supplier'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICashReceiptVoucherSupplier>>): void {
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

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { IGoodsReceipt, GoodsReceipt } from 'app/shared/model/goods-receipt.model';
import { GoodsReceiptService } from './goods-receipt.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService } from 'app/entities/supplier/supplier.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';
import { IPaymentTypes } from 'app/shared/model/payment-types.model';
import { PaymentTypesService } from 'app/entities/payment-types/payment-types.service';

type SelectableEntity = IDocumentHistory | ISupplier | ILocation | ITransactionType | IPaymentTypes;

@Component({
  selector: 'jhi-goods-receipt-update',
  templateUrl: './goods-receipt-update.component.html'
})
export class GoodsReceiptUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  suppliers: ISupplier[] = [];

  locations: ILocation[] = [];

  transactiontypes: ITransactionType[] = [];

  paymenttypes: IPaymentTypes[] = [];
  grnDateDp: any;

  editForm = this.fb.group({
    id: [],
    grnNumber: [null, [Validators.required]],
    grnDate: [null, [Validators.required]],
    poNumber: [],
    grnAmount: [null, [Validators.required]],
    history: [],
    supplier: [null, Validators.required],
    location: [null, Validators.required],
    transactionType: [null, Validators.required],
    payType: [null, Validators.required]
  });

  constructor(
    protected goodsReceiptService: GoodsReceiptService,
    protected documentHistoryService: DocumentHistoryService,
    protected supplierService: SupplierService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected paymentTypesService: PaymentTypesService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ goodsReceipt }) => {
      this.updateForm(goodsReceipt);

      this.documentHistoryService
        .query({ 'goodsReceiptId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!goodsReceipt.history || !goodsReceipt.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(goodsReceipt.history.id)
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

      this.supplierService
        .query()
        .pipe(
          map((res: HttpResponse<ISupplier[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ISupplier[]) => (this.suppliers = resBody));

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

  updateForm(goodsReceipt: IGoodsReceipt): void {
    this.editForm.patchValue({
      id: goodsReceipt.id,
      grnNumber: goodsReceipt.grnNumber,
      grnDate: goodsReceipt.grnDate,
      poNumber: goodsReceipt.poNumber,
      grnAmount: goodsReceipt.grnAmount,
      history: goodsReceipt.history,
      supplier: goodsReceipt.supplier,
      location: goodsReceipt.location,
      transactionType: goodsReceipt.transactionType,
      payType: goodsReceipt.payType
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const goodsReceipt = this.createFromForm();
    if (goodsReceipt.id !== undefined) {
      this.subscribeToSaveResponse(this.goodsReceiptService.update(goodsReceipt));
    } else {
      this.subscribeToSaveResponse(this.goodsReceiptService.create(goodsReceipt));
    }
  }

  private createFromForm(): IGoodsReceipt {
    return {
      ...new GoodsReceipt(),
      id: this.editForm.get(['id'])!.value,
      grnNumber: this.editForm.get(['grnNumber'])!.value,
      grnDate: this.editForm.get(['grnDate'])!.value,
      poNumber: this.editForm.get(['poNumber'])!.value,
      grnAmount: this.editForm.get(['grnAmount'])!.value,
      history: this.editForm.get(['history'])!.value,
      supplier: this.editForm.get(['supplier'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      payType: this.editForm.get(['payType'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGoodsReceipt>>): void {
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

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { IPurchaseOrder, PurchaseOrder } from 'app/shared/model/purchase-order.model';
import { PurchaseOrderService } from './purchase-order.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService } from 'app/entities/supplier/supplier.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';
import { IGoodsReceipt } from 'app/shared/model/goods-receipt.model';
import { GoodsReceiptService } from 'app/entities/goods-receipt/goods-receipt.service';

type SelectableEntity = IDocumentHistory | ISupplier | ILocation | ITransactionType | IGoodsReceipt;

@Component({
  selector: 'jhi-purchase-order-update',
  templateUrl: './purchase-order-update.component.html'
})
export class PurchaseOrderUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  suppliers: ISupplier[] = [];

  locations: ILocation[] = [];

  transactiontypes: ITransactionType[] = [];

  goodsreceipts: IGoodsReceipt[] = [];
  poDateDp: any;

  editForm = this.fb.group({
    id: [],
    poNumber: [null, [Validators.required]],
    poDate: [null, [Validators.required]],
    poAmount: [],
    history: [],
    supplier: [null, Validators.required],
    location: [null, Validators.required],
    transactionType: [null, Validators.required],
    relatedGRN: []
  });

  constructor(
    protected purchaseOrderService: PurchaseOrderService,
    protected documentHistoryService: DocumentHistoryService,
    protected supplierService: SupplierService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected goodsReceiptService: GoodsReceiptService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaseOrder }) => {
      this.updateForm(purchaseOrder);

      this.documentHistoryService
        .query({ 'purchaseOrderId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!purchaseOrder.history || !purchaseOrder.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(purchaseOrder.history.id)
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

      this.goodsReceiptService
        .query()
        .pipe(
          map((res: HttpResponse<IGoodsReceipt[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IGoodsReceipt[]) => (this.goodsreceipts = resBody));
    });
  }

  updateForm(purchaseOrder: IPurchaseOrder): void {
    this.editForm.patchValue({
      id: purchaseOrder.id,
      poNumber: purchaseOrder.poNumber,
      poDate: purchaseOrder.poDate,
      poAmount: purchaseOrder.poAmount,
      history: purchaseOrder.history,
      supplier: purchaseOrder.supplier,
      location: purchaseOrder.location,
      transactionType: purchaseOrder.transactionType,
      relatedGRN: purchaseOrder.relatedGRN
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const purchaseOrder = this.createFromForm();
    if (purchaseOrder.id !== undefined) {
      this.subscribeToSaveResponse(this.purchaseOrderService.update(purchaseOrder));
    } else {
      this.subscribeToSaveResponse(this.purchaseOrderService.create(purchaseOrder));
    }
  }

  private createFromForm(): IPurchaseOrder {
    return {
      ...new PurchaseOrder(),
      id: this.editForm.get(['id'])!.value,
      poNumber: this.editForm.get(['poNumber'])!.value,
      poDate: this.editForm.get(['poDate'])!.value,
      poAmount: this.editForm.get(['poAmount'])!.value,
      history: this.editForm.get(['history'])!.value,
      supplier: this.editForm.get(['supplier'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      relatedGRN: this.editForm.get(['relatedGRN'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaseOrder>>): void {
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

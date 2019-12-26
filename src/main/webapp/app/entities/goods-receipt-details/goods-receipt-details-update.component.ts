import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IGoodsReceiptDetails, GoodsReceiptDetails } from 'app/shared/model/goods-receipt-details.model';
import { GoodsReceiptDetailsService } from './goods-receipt-details.service';
import { IItems } from 'app/shared/model/items.model';
import { ItemsService } from 'app/entities/items/items.service';
import { IStorageBin } from 'app/shared/model/storage-bin.model';
import { StorageBinService } from 'app/entities/storage-bin/storage-bin.service';
import { IGoodsReceipt } from 'app/shared/model/goods-receipt.model';
import { GoodsReceiptService } from 'app/entities/goods-receipt/goods-receipt.service';

type SelectableEntity = IItems | IStorageBin | IGoodsReceipt;

@Component({
  selector: 'jhi-goods-receipt-details-update',
  templateUrl: './goods-receipt-details-update.component.html'
})
export class GoodsReceiptDetailsUpdateComponent implements OnInit {
  isSaving = false;

  items: IItems[] = [];

  storagebins: IStorageBin[] = [];

  goodsreceipts: IGoodsReceipt[] = [];

  editForm = this.fb.group({
    id: [],
    grnQty: [null, [Validators.required]],
    revisedItemCost: [],
    item: [null, Validators.required],
    storageBin: [],
    grn: [null, Validators.required]
  });

  constructor(
    protected goodsReceiptDetailsService: GoodsReceiptDetailsService,
    protected itemsService: ItemsService,
    protected storageBinService: StorageBinService,
    protected goodsReceiptService: GoodsReceiptService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ goodsReceiptDetails }) => {
      this.updateForm(goodsReceiptDetails);

      this.itemsService
        .query()
        .pipe(
          map((res: HttpResponse<IItems[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IItems[]) => (this.items = resBody));

      this.storageBinService
        .query()
        .pipe(
          map((res: HttpResponse<IStorageBin[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IStorageBin[]) => (this.storagebins = resBody));

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

  updateForm(goodsReceiptDetails: IGoodsReceiptDetails): void {
    this.editForm.patchValue({
      id: goodsReceiptDetails.id,
      grnQty: goodsReceiptDetails.grnQty,
      revisedItemCost: goodsReceiptDetails.revisedItemCost,
      item: goodsReceiptDetails.item,
      storageBin: goodsReceiptDetails.storageBin,
      grn: goodsReceiptDetails.grn
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const goodsReceiptDetails = this.createFromForm();
    if (goodsReceiptDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.goodsReceiptDetailsService.update(goodsReceiptDetails));
    } else {
      this.subscribeToSaveResponse(this.goodsReceiptDetailsService.create(goodsReceiptDetails));
    }
  }

  private createFromForm(): IGoodsReceiptDetails {
    return {
      ...new GoodsReceiptDetails(),
      id: this.editForm.get(['id'])!.value,
      grnQty: this.editForm.get(['grnQty'])!.value,
      revisedItemCost: this.editForm.get(['revisedItemCost'])!.value,
      item: this.editForm.get(['item'])!.value,
      storageBin: this.editForm.get(['storageBin'])!.value,
      grn: this.editForm.get(['grn'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGoodsReceiptDetails>>): void {
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

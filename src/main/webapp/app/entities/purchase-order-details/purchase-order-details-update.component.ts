import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IPurchaseOrderDetails, PurchaseOrderDetails } from 'app/shared/model/purchase-order-details.model';
import { PurchaseOrderDetailsService } from './purchase-order-details.service';
import { IItems } from 'app/shared/model/items.model';
import { ItemsService } from 'app/entities/items/items.service';
import { IPurchaseOrder } from 'app/shared/model/purchase-order.model';
import { PurchaseOrderService } from 'app/entities/purchase-order/purchase-order.service';

type SelectableEntity = IItems | IPurchaseOrder;

@Component({
  selector: 'jhi-purchase-order-details-update',
  templateUrl: './purchase-order-details-update.component.html'
})
export class PurchaseOrderDetailsUpdateComponent implements OnInit {
  isSaving = false;

  items: IItems[] = [];

  purchaseorders: IPurchaseOrder[] = [];

  editForm = this.fb.group({
    id: [],
    itemQty: [null, [Validators.required]],
    item: [null, Validators.required],
    po: [null, Validators.required]
  });

  constructor(
    protected purchaseOrderDetailsService: PurchaseOrderDetailsService,
    protected itemsService: ItemsService,
    protected purchaseOrderService: PurchaseOrderService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaseOrderDetails }) => {
      this.updateForm(purchaseOrderDetails);

      this.itemsService
        .query()
        .pipe(
          map((res: HttpResponse<IItems[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IItems[]) => (this.items = resBody));

      this.purchaseOrderService
        .query()
        .pipe(
          map((res: HttpResponse<IPurchaseOrder[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IPurchaseOrder[]) => (this.purchaseorders = resBody));
    });
  }

  updateForm(purchaseOrderDetails: IPurchaseOrderDetails): void {
    this.editForm.patchValue({
      id: purchaseOrderDetails.id,
      itemQty: purchaseOrderDetails.itemQty,
      item: purchaseOrderDetails.item,
      po: purchaseOrderDetails.po
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const purchaseOrderDetails = this.createFromForm();
    if (purchaseOrderDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.purchaseOrderDetailsService.update(purchaseOrderDetails));
    } else {
      this.subscribeToSaveResponse(this.purchaseOrderDetailsService.create(purchaseOrderDetails));
    }
  }

  private createFromForm(): IPurchaseOrderDetails {
    return {
      ...new PurchaseOrderDetails(),
      id: this.editForm.get(['id'])!.value,
      itemQty: this.editForm.get(['itemQty'])!.value,
      item: this.editForm.get(['item'])!.value,
      po: this.editForm.get(['po'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaseOrderDetails>>): void {
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

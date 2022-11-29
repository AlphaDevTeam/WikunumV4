import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { IStockTransfer, StockTransfer } from 'app/shared/model/stock-transfer.model';
import { StockTransferService } from './stock-transfer.service';
import { IItems } from 'app/shared/model/items.model';
import { ItemsService } from 'app/entities/items/items.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

type SelectableEntity = IItems | ILocation;

@Component({
  selector: 'jhi-stock-transfer-update',
  templateUrl: './stock-transfer-update.component.html'
})
export class StockTransferUpdateComponent implements OnInit {
  isSaving = false;

  items: IItems[] = [];

  locations: ILocation[] = [];
  transactionDateDp: any;

  editForm = this.fb.group({
    id: [],
    transactionNumber: [null, [Validators.required]],
    transactionDate: [null, [Validators.required]],
    transactionDescription: [null, [Validators.required]],
    transactionQty: [null, [Validators.required]],
    item: [null, Validators.required],
    locationFrom: [null, Validators.required],
    locationTo: [null, Validators.required]
  });

  constructor(
    protected stockTransferService: StockTransferService,
    protected itemsService: ItemsService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stockTransfer }) => {
      this.updateForm(stockTransfer);

      this.itemsService
        .query()
        .pipe(
          map((res: HttpResponse<IItems[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IItems[]) => (this.items = resBody));

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

  updateForm(stockTransfer: IStockTransfer): void {
    this.editForm.patchValue({
      id: stockTransfer.id,
      transactionNumber: stockTransfer.transactionNumber,
      transactionDate: stockTransfer.transactionDate,
      transactionDescription: stockTransfer.transactionDescription,
      transactionQty: stockTransfer.transactionQty,
      item: stockTransfer.item,
      locationFrom: stockTransfer.locationFrom,
      locationTo: stockTransfer.locationTo
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stockTransfer = this.createFromForm();
    if (stockTransfer.id !== undefined) {
      this.subscribeToSaveResponse(this.stockTransferService.update(stockTransfer));
    } else {
      this.subscribeToSaveResponse(this.stockTransferService.create(stockTransfer));
    }
  }

  private createFromForm(): IStockTransfer {
    return {
      ...new StockTransfer(),
      id: this.editForm.get(['id'])!.value,
      transactionNumber: this.editForm.get(['transactionNumber'])!.value,
      transactionDate: this.editForm.get(['transactionDate'])!.value,
      transactionDescription: this.editForm.get(['transactionDescription'])!.value,
      transactionQty: this.editForm.get(['transactionQty'])!.value,
      item: this.editForm.get(['item'])!.value,
      locationFrom: this.editForm.get(['locationFrom'])!.value,
      locationTo: this.editForm.get(['locationTo'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStockTransfer>>): void {
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

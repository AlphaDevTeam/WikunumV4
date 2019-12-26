import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { IItemBinCard, ItemBinCard } from 'app/shared/model/item-bin-card.model';
import { ItemBinCardService } from './item-bin-card.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { IItems } from 'app/shared/model/items.model';
import { ItemsService } from 'app/entities/items/items.service';

type SelectableEntity = IDocumentHistory | ILocation | IItems;

@Component({
  selector: 'jhi-item-bin-card-update',
  templateUrl: './item-bin-card-update.component.html'
})
export class ItemBinCardUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  locations: ILocation[] = [];

  items: IItems[] = [];
  transactionDateDp: any;

  editForm = this.fb.group({
    id: [],
    transactionDate: [null, [Validators.required]],
    transactionDescription: [null, [Validators.required]],
    transactionQty: [null, [Validators.required]],
    transactionBalance: [null, [Validators.required]],
    history: [],
    location: [null, Validators.required],
    item: [null, Validators.required]
  });

  constructor(
    protected itemBinCardService: ItemBinCardService,
    protected documentHistoryService: DocumentHistoryService,
    protected locationService: LocationService,
    protected itemsService: ItemsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemBinCard }) => {
      this.updateForm(itemBinCard);

      this.documentHistoryService
        .query({ 'itemBinCardId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!itemBinCard.history || !itemBinCard.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(itemBinCard.history.id)
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

      this.itemsService
        .query()
        .pipe(
          map((res: HttpResponse<IItems[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IItems[]) => (this.items = resBody));
    });
  }

  updateForm(itemBinCard: IItemBinCard): void {
    this.editForm.patchValue({
      id: itemBinCard.id,
      transactionDate: itemBinCard.transactionDate,
      transactionDescription: itemBinCard.transactionDescription,
      transactionQty: itemBinCard.transactionQty,
      transactionBalance: itemBinCard.transactionBalance,
      history: itemBinCard.history,
      location: itemBinCard.location,
      item: itemBinCard.item
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const itemBinCard = this.createFromForm();
    if (itemBinCard.id !== undefined) {
      this.subscribeToSaveResponse(this.itemBinCardService.update(itemBinCard));
    } else {
      this.subscribeToSaveResponse(this.itemBinCardService.create(itemBinCard));
    }
  }

  private createFromForm(): IItemBinCard {
    return {
      ...new ItemBinCard(),
      id: this.editForm.get(['id'])!.value,
      transactionDate: this.editForm.get(['transactionDate'])!.value,
      transactionDescription: this.editForm.get(['transactionDescription'])!.value,
      transactionQty: this.editForm.get(['transactionQty'])!.value,
      transactionBalance: this.editForm.get(['transactionBalance'])!.value,
      history: this.editForm.get(['history'])!.value,
      location: this.editForm.get(['location'])!.value,
      item: this.editForm.get(['item'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemBinCard>>): void {
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

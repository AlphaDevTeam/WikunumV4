import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IItems, Items } from 'app/shared/model/items.model';
import { ItemsService } from './items.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';
import { IModel } from 'app/shared/model/model.model';
import { ModelService } from 'app/entities/model/model.service';
import { IProducts } from 'app/shared/model/products.model';
import { ProductsService } from 'app/entities/products/products.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { IUnitOfMeasure } from 'app/shared/model/unit-of-measure.model';
import { UnitOfMeasureService } from 'app/entities/unit-of-measure/unit-of-measure.service';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/entities/currency/currency.service';
import { IItemAddOns } from 'app/shared/model/item-add-ons.model';
import { ItemAddOnsService } from 'app/entities/item-add-ons/item-add-ons.service';

type SelectableEntity = IDocumentHistory | IModel | IProducts | ILocation | IUnitOfMeasure | ICurrency | IItemAddOns;

@Component({
  selector: 'jhi-items-update',
  templateUrl: './items-update.component.html'
})
export class ItemsUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  models: IModel[] = [];

  products: IProducts[] = [];

  locations: ILocation[] = [];

  unitofmeasures: IUnitOfMeasure[] = [];

  currencies: ICurrency[] = [];

  itemaddons: IItemAddOns[] = [];
  originalStockDateDp: any;
  modifiedStockDateDp: any;

  editForm = this.fb.group({
    id: [],
    itemCode: [null, [Validators.required]],
    itemName: [null, [Validators.required]],
    itemDescription: [],
    itemPrice: [],
    itemSerial: [],
    itemSupplierSerial: [],
    itemPromotionalPrice: [],
    itemPromotionalPercentage: [],
    itemCost: [null, [Validators.required]],
    originalStockDate: [null, [Validators.required]],
    modifiedStockDate: [null, [Validators.required]],
    image: [],
    imageContentType: [],
    history: [],
    relatedModel: [null, Validators.required],
    relatedProduct: [null, Validators.required],
    location: [null, Validators.required],
    unitOfMeasure: [null, Validators.required],
    currency: [null, Validators.required],
    addons: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected itemsService: ItemsService,
    protected documentHistoryService: DocumentHistoryService,
    protected modelService: ModelService,
    protected productsService: ProductsService,
    protected locationService: LocationService,
    protected unitOfMeasureService: UnitOfMeasureService,
    protected currencyService: CurrencyService,
    protected itemAddOnsService: ItemAddOnsService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ items }) => {
      this.updateForm(items);

      this.documentHistoryService
        .query({ 'itemsId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!items.history || !items.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(items.history.id)
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

      this.modelService
        .query()
        .pipe(
          map((res: HttpResponse<IModel[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IModel[]) => (this.models = resBody));

      this.productsService
        .query()
        .pipe(
          map((res: HttpResponse<IProducts[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IProducts[]) => (this.products = resBody));

      this.locationService
        .query()
        .pipe(
          map((res: HttpResponse<ILocation[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ILocation[]) => (this.locations = resBody));

      this.unitOfMeasureService
        .query()
        .pipe(
          map((res: HttpResponse<IUnitOfMeasure[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IUnitOfMeasure[]) => (this.unitofmeasures = resBody));

      this.currencyService
        .query()
        .pipe(
          map((res: HttpResponse<ICurrency[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ICurrency[]) => (this.currencies = resBody));

      this.itemAddOnsService
        .query()
        .pipe(
          map((res: HttpResponse<IItemAddOns[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IItemAddOns[]) => (this.itemaddons = resBody));
    });
  }

  updateForm(items: IItems): void {
    this.editForm.patchValue({
      id: items.id,
      itemCode: items.itemCode,
      itemName: items.itemName,
      itemDescription: items.itemDescription,
      itemPrice: items.itemPrice,
      itemSerial: items.itemSerial,
      itemSupplierSerial: items.itemSupplierSerial,
      itemPromotionalPrice: items.itemPromotionalPrice,
      itemPromotionalPercentage: items.itemPromotionalPercentage,
      itemCost: items.itemCost,
      originalStockDate: items.originalStockDate,
      modifiedStockDate: items.modifiedStockDate,
      image: items.image,
      imageContentType: items.imageContentType,
      history: items.history,
      relatedModel: items.relatedModel,
      relatedProduct: items.relatedProduct,
      location: items.location,
      unitOfMeasure: items.unitOfMeasure,
      currency: items.currency,
      addons: items.addons
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('wikunumApp.error', { message: err.message })
      );
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const items = this.createFromForm();
    if (items.id !== undefined) {
      this.subscribeToSaveResponse(this.itemsService.update(items));
    } else {
      this.subscribeToSaveResponse(this.itemsService.create(items));
    }
  }

  private createFromForm(): IItems {
    return {
      ...new Items(),
      id: this.editForm.get(['id'])!.value,
      itemCode: this.editForm.get(['itemCode'])!.value,
      itemName: this.editForm.get(['itemName'])!.value,
      itemDescription: this.editForm.get(['itemDescription'])!.value,
      itemPrice: this.editForm.get(['itemPrice'])!.value,
      itemSerial: this.editForm.get(['itemSerial'])!.value,
      itemSupplierSerial: this.editForm.get(['itemSupplierSerial'])!.value,
      itemPromotionalPrice: this.editForm.get(['itemPromotionalPrice'])!.value,
      itemPromotionalPercentage: this.editForm.get(['itemPromotionalPercentage'])!.value,
      itemCost: this.editForm.get(['itemCost'])!.value,
      originalStockDate: this.editForm.get(['originalStockDate'])!.value,
      modifiedStockDate: this.editForm.get(['modifiedStockDate'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      history: this.editForm.get(['history'])!.value,
      relatedModel: this.editForm.get(['relatedModel'])!.value,
      relatedProduct: this.editForm.get(['relatedProduct'])!.value,
      location: this.editForm.get(['location'])!.value,
      unitOfMeasure: this.editForm.get(['unitOfMeasure'])!.value,
      currency: this.editForm.get(['currency'])!.value,
      addons: this.editForm.get(['addons'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItems>>): void {
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

  getSelected(selectedVals: IItemAddOns[], option: IItemAddOns): IItemAddOns {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}

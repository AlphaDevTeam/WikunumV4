import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IModel, Model } from 'app/shared/model/model.model';
import { ModelService } from './model.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';
import { IProducts } from 'app/shared/model/products.model';
import { ProductsService } from 'app/entities/products/products.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

type SelectableEntity = IDocumentHistory | IProducts | ILocation;

@Component({
  selector: 'jhi-model-update',
  templateUrl: './model-update.component.html'
})
export class ModelUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  products: IProducts[] = [];

  locations: ILocation[] = [];

  editForm = this.fb.group({
    id: [],
    modelCode: [null, [Validators.required]],
    modelName: [null, [Validators.required]],
    history: [],
    relatedProduct: [null, Validators.required],
    location: [null, Validators.required]
  });

  constructor(
    protected modelService: ModelService,
    protected documentHistoryService: DocumentHistoryService,
    protected productsService: ProductsService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ model }) => {
      this.updateForm(model);

      this.documentHistoryService
        .query({ 'modelId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!model.history || !model.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(model.history.id)
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
    });
  }

  updateForm(model: IModel): void {
    this.editForm.patchValue({
      id: model.id,
      modelCode: model.modelCode,
      modelName: model.modelName,
      history: model.history,
      relatedProduct: model.relatedProduct,
      location: model.location
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const model = this.createFromForm();
    if (model.id !== undefined) {
      this.subscribeToSaveResponse(this.modelService.update(model));
    } else {
      this.subscribeToSaveResponse(this.modelService.create(model));
    }
  }

  private createFromForm(): IModel {
    return {
      ...new Model(),
      id: this.editForm.get(['id'])!.value,
      modelCode: this.editForm.get(['modelCode'])!.value,
      modelName: this.editForm.get(['modelName'])!.value,
      history: this.editForm.get(['history'])!.value,
      relatedProduct: this.editForm.get(['relatedProduct'])!.value,
      location: this.editForm.get(['location'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IModel>>): void {
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
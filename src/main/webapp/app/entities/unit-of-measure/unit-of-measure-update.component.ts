import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IUnitOfMeasure, UnitOfMeasure } from 'app/shared/model/unit-of-measure.model';
import { UnitOfMeasureService } from './unit-of-measure.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';

@Component({
  selector: 'jhi-unit-of-measure-update',
  templateUrl: './unit-of-measure-update.component.html'
})
export class UnitOfMeasureUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  editForm = this.fb.group({
    id: [],
    unitOfMeasureCode: [null, [Validators.required]],
    unitOfMeasureDescription: [null, [Validators.required]],
    history: []
  });

  constructor(
    protected unitOfMeasureService: UnitOfMeasureService,
    protected documentHistoryService: DocumentHistoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ unitOfMeasure }) => {
      this.updateForm(unitOfMeasure);

      this.documentHistoryService
        .query({ 'unitOfMeasureId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!unitOfMeasure.history || !unitOfMeasure.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(unitOfMeasure.history.id)
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
    });
  }

  updateForm(unitOfMeasure: IUnitOfMeasure): void {
    this.editForm.patchValue({
      id: unitOfMeasure.id,
      unitOfMeasureCode: unitOfMeasure.unitOfMeasureCode,
      unitOfMeasureDescription: unitOfMeasure.unitOfMeasureDescription,
      history: unitOfMeasure.history
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const unitOfMeasure = this.createFromForm();
    if (unitOfMeasure.id !== undefined) {
      this.subscribeToSaveResponse(this.unitOfMeasureService.update(unitOfMeasure));
    } else {
      this.subscribeToSaveResponse(this.unitOfMeasureService.create(unitOfMeasure));
    }
  }

  private createFromForm(): IUnitOfMeasure {
    return {
      ...new UnitOfMeasure(),
      id: this.editForm.get(['id'])!.value,
      unitOfMeasureCode: this.editForm.get(['unitOfMeasureCode'])!.value,
      unitOfMeasureDescription: this.editForm.get(['unitOfMeasureDescription'])!.value,
      history: this.editForm.get(['history'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUnitOfMeasure>>): void {
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

  trackById(index: number, item: IDocumentHistory): any {
    return item.id;
  }
}

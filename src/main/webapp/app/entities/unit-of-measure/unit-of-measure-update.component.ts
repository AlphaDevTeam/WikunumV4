import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IUnitOfMeasure, UnitOfMeasure } from 'app/shared/model/unit-of-measure.model';
import { UnitOfMeasureService } from './unit-of-measure.service';

@Component({
  selector: 'jhi-unit-of-measure-update',
  templateUrl: './unit-of-measure-update.component.html'
})
export class UnitOfMeasureUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    unitOfMeasureCode: [null, [Validators.required]],
    unitOfMeasureDescription: [null, [Validators.required]],
    isActive: []
  });

  constructor(protected unitOfMeasureService: UnitOfMeasureService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ unitOfMeasure }) => {
      this.updateForm(unitOfMeasure);
    });
  }

  updateForm(unitOfMeasure: IUnitOfMeasure): void {
    this.editForm.patchValue({
      id: unitOfMeasure.id,
      unitOfMeasureCode: unitOfMeasure.unitOfMeasureCode,
      unitOfMeasureDescription: unitOfMeasure.unitOfMeasureDescription,
      isActive: unitOfMeasure.isActive
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
      isActive: this.editForm.get(['isActive'])!.value
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
}

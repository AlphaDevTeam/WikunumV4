import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILicenseType, LicenseType } from 'app/shared/model/license-type.model';
import { LicenseTypeService } from './license-type.service';

@Component({
  selector: 'jhi-license-type-update',
  templateUrl: './license-type-update.component.html'
})
export class LicenseTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    licenseTypeCode: [null, [Validators.required]],
    licenseTypeName: [null, [Validators.required]],
    validityDays: [],
    isActive: []
  });

  constructor(protected licenseTypeService: LicenseTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ licenseType }) => {
      this.updateForm(licenseType);
    });
  }

  updateForm(licenseType: ILicenseType): void {
    this.editForm.patchValue({
      id: licenseType.id,
      licenseTypeCode: licenseType.licenseTypeCode,
      licenseTypeName: licenseType.licenseTypeName,
      validityDays: licenseType.validityDays,
      isActive: licenseType.isActive
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const licenseType = this.createFromForm();
    if (licenseType.id !== undefined) {
      this.subscribeToSaveResponse(this.licenseTypeService.update(licenseType));
    } else {
      this.subscribeToSaveResponse(this.licenseTypeService.create(licenseType));
    }
  }

  private createFromForm(): ILicenseType {
    return {
      ...new LicenseType(),
      id: this.editForm.get(['id'])!.value,
      licenseTypeCode: this.editForm.get(['licenseTypeCode'])!.value,
      licenseTypeName: this.editForm.get(['licenseTypeName'])!.value,
      validityDays: this.editForm.get(['validityDays'])!.value,
      isActive: this.editForm.get(['isActive'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILicenseType>>): void {
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

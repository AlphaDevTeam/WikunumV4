import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IEmployeeType, EmployeeType } from 'app/shared/model/employee-type.model';
import { EmployeeTypeService } from './employee-type.service';

@Component({
  selector: 'jhi-employee-type-update',
  templateUrl: './employee-type-update.component.html'
})
export class EmployeeTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    employeeTypeCode: [null, [Validators.required]],
    employeeTypeName: [null, [Validators.required]],
    isActive: []
  });

  constructor(protected employeeTypeService: EmployeeTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeType }) => {
      this.updateForm(employeeType);
    });
  }

  updateForm(employeeType: IEmployeeType): void {
    this.editForm.patchValue({
      id: employeeType.id,
      employeeTypeCode: employeeType.employeeTypeCode,
      employeeTypeName: employeeType.employeeTypeName,
      isActive: employeeType.isActive
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeeType = this.createFromForm();
    if (employeeType.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeTypeService.update(employeeType));
    } else {
      this.subscribeToSaveResponse(this.employeeTypeService.create(employeeType));
    }
  }

  private createFromForm(): IEmployeeType {
    return {
      ...new EmployeeType(),
      id: this.editForm.get(['id'])!.value,
      employeeTypeCode: this.editForm.get(['employeeTypeCode'])!.value,
      employeeTypeName: this.editForm.get(['employeeTypeName'])!.value,
      isActive: this.editForm.get(['isActive'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeeType>>): void {
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

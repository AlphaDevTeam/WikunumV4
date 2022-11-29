import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IPaymentTypes, PaymentTypes } from 'app/shared/model/payment-types.model';
import { PaymentTypesService } from './payment-types.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

@Component({
  selector: 'jhi-payment-types-update',
  templateUrl: './payment-types-update.component.html'
})
export class PaymentTypesUpdateComponent implements OnInit {
  isSaving = false;

  locations: ILocation[] = [];

  editForm = this.fb.group({
    id: [],
    paymentTypesCode: [null, [Validators.required]],
    paymentTypes: [null, [Validators.required]],
    paymentTypesChargePer: [],
    isActive: [],
    location: [null, Validators.required]
  });

  constructor(
    protected paymentTypesService: PaymentTypesService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentTypes }) => {
      this.updateForm(paymentTypes);

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

  updateForm(paymentTypes: IPaymentTypes): void {
    this.editForm.patchValue({
      id: paymentTypes.id,
      paymentTypesCode: paymentTypes.paymentTypesCode,
      paymentTypes: paymentTypes.paymentTypes,
      paymentTypesChargePer: paymentTypes.paymentTypesChargePer,
      isActive: paymentTypes.isActive,
      location: paymentTypes.location
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paymentTypes = this.createFromForm();
    if (paymentTypes.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentTypesService.update(paymentTypes));
    } else {
      this.subscribeToSaveResponse(this.paymentTypesService.create(paymentTypes));
    }
  }

  private createFromForm(): IPaymentTypes {
    return {
      ...new PaymentTypes(),
      id: this.editForm.get(['id'])!.value,
      paymentTypesCode: this.editForm.get(['paymentTypesCode'])!.value,
      paymentTypes: this.editForm.get(['paymentTypes'])!.value,
      paymentTypesChargePer: this.editForm.get(['paymentTypesChargePer'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      location: this.editForm.get(['location'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentTypes>>): void {
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

  trackById(index: number, item: ILocation): any {
    return item.id;
  }
}

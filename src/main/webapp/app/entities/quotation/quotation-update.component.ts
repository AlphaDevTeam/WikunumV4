import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { IQuotation, Quotation } from 'app/shared/model/quotation.model';
import { QuotationService } from './quotation.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

@Component({
  selector: 'jhi-quotation-update',
  templateUrl: './quotation-update.component.html'
})
export class QuotationUpdateComponent implements OnInit {
  isSaving = false;

  locations: ILocation[] = [];
  quotationDateDp: any;
  quotationexpireDateDp: any;

  editForm = this.fb.group({
    id: [],
    quotationNumber: [null, [Validators.required]],
    quotationDate: [null, [Validators.required]],
    quotationexpireDate: [null, [Validators.required]],
    quotationTotalAmount: [],
    quotationTo: [null, [Validators.required]],
    quotationFrom: [null, [Validators.required]],
    projectNumber: [null, [Validators.required]],
    quotationNote: [],
    location: [null, Validators.required]
  });

  constructor(
    protected quotationService: QuotationService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quotation }) => {
      this.updateForm(quotation);

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

  updateForm(quotation: IQuotation): void {
    this.editForm.patchValue({
      id: quotation.id,
      quotationNumber: quotation.quotationNumber,
      quotationDate: quotation.quotationDate,
      quotationexpireDate: quotation.quotationexpireDate,
      quotationTotalAmount: quotation.quotationTotalAmount,
      quotationTo: quotation.quotationTo,
      quotationFrom: quotation.quotationFrom,
      projectNumber: quotation.projectNumber,
      quotationNote: quotation.quotationNote,
      location: quotation.location
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quotation = this.createFromForm();
    if (quotation.id !== undefined) {
      this.subscribeToSaveResponse(this.quotationService.update(quotation));
    } else {
      this.subscribeToSaveResponse(this.quotationService.create(quotation));
    }
  }

  private createFromForm(): IQuotation {
    return {
      ...new Quotation(),
      id: this.editForm.get(['id'])!.value,
      quotationNumber: this.editForm.get(['quotationNumber'])!.value,
      quotationDate: this.editForm.get(['quotationDate'])!.value,
      quotationexpireDate: this.editForm.get(['quotationexpireDate'])!.value,
      quotationTotalAmount: this.editForm.get(['quotationTotalAmount'])!.value,
      quotationTo: this.editForm.get(['quotationTo'])!.value,
      quotationFrom: this.editForm.get(['quotationFrom'])!.value,
      projectNumber: this.editForm.get(['projectNumber'])!.value,
      quotationNote: this.editForm.get(['quotationNote'])!.value,
      location: this.editForm.get(['location'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuotation>>): void {
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

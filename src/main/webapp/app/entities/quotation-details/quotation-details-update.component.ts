import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IQuotationDetails, QuotationDetails } from 'app/shared/model/quotation-details.model';
import { QuotationDetailsService } from './quotation-details.service';
import { IItems } from 'app/shared/model/items.model';
import { ItemsService } from 'app/entities/items/items.service';
import { IQuotation } from 'app/shared/model/quotation.model';
import { QuotationService } from 'app/entities/quotation/quotation.service';

type SelectableEntity = IItems | IQuotation;

@Component({
  selector: 'jhi-quotation-details-update',
  templateUrl: './quotation-details-update.component.html'
})
export class QuotationDetailsUpdateComponent implements OnInit {
  isSaving = false;

  items: IItems[] = [];

  quotations: IQuotation[] = [];

  editForm = this.fb.group({
    id: [],
    rate: [null, [Validators.required]],
    description: [],
    item: [null, Validators.required],
    quote: [null, Validators.required]
  });

  constructor(
    protected quotationDetailsService: QuotationDetailsService,
    protected itemsService: ItemsService,
    protected quotationService: QuotationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quotationDetails }) => {
      this.updateForm(quotationDetails);

      this.itemsService
        .query()
        .pipe(
          map((res: HttpResponse<IItems[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IItems[]) => (this.items = resBody));

      this.quotationService
        .query()
        .pipe(
          map((res: HttpResponse<IQuotation[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IQuotation[]) => (this.quotations = resBody));
    });
  }

  updateForm(quotationDetails: IQuotationDetails): void {
    this.editForm.patchValue({
      id: quotationDetails.id,
      rate: quotationDetails.rate,
      description: quotationDetails.description,
      item: quotationDetails.item,
      quote: quotationDetails.quote
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quotationDetails = this.createFromForm();
    if (quotationDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.quotationDetailsService.update(quotationDetails));
    } else {
      this.subscribeToSaveResponse(this.quotationDetailsService.create(quotationDetails));
    }
  }

  private createFromForm(): IQuotationDetails {
    return {
      ...new QuotationDetails(),
      id: this.editForm.get(['id'])!.value,
      rate: this.editForm.get(['rate'])!.value,
      description: this.editForm.get(['description'])!.value,
      item: this.editForm.get(['item'])!.value,
      quote: this.editForm.get(['quote'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuotationDetails>>): void {
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

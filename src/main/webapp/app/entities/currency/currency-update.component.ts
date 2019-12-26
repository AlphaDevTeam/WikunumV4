import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ICurrency, Currency } from 'app/shared/model/currency.model';
import { CurrencyService } from './currency.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';

@Component({
  selector: 'jhi-currency-update',
  templateUrl: './currency-update.component.html'
})
export class CurrencyUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  editForm = this.fb.group({
    id: [],
    currencyCode: [null, [Validators.required]],
    currencyName: [null, [Validators.required]],
    history: []
  });

  constructor(
    protected currencyService: CurrencyService,
    protected documentHistoryService: DocumentHistoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ currency }) => {
      this.updateForm(currency);

      this.documentHistoryService
        .query({ 'currencyId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!currency.history || !currency.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(currency.history.id)
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

  updateForm(currency: ICurrency): void {
    this.editForm.patchValue({
      id: currency.id,
      currencyCode: currency.currencyCode,
      currencyName: currency.currencyName,
      history: currency.history
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const currency = this.createFromForm();
    if (currency.id !== undefined) {
      this.subscribeToSaveResponse(this.currencyService.update(currency));
    } else {
      this.subscribeToSaveResponse(this.currencyService.create(currency));
    }
  }

  private createFromForm(): ICurrency {
    return {
      ...new Currency(),
      id: this.editForm.get(['id'])!.value,
      currencyCode: this.editForm.get(['currencyCode'])!.value,
      currencyName: this.editForm.get(['currencyName'])!.value,
      history: this.editForm.get(['history'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICurrency>>): void {
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

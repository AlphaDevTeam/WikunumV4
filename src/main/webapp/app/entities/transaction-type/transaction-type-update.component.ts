import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ITransactionType, TransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from './transaction-type.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';

@Component({
  selector: 'jhi-transaction-type-update',
  templateUrl: './transaction-type-update.component.html'
})
export class TransactionTypeUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  editForm = this.fb.group({
    id: [],
    transactionTypeCode: [null, [Validators.required]],
    transactionType: [null, [Validators.required]],
    isActive: [],
    history: []
  });

  constructor(
    protected transactionTypeService: TransactionTypeService,
    protected documentHistoryService: DocumentHistoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionType }) => {
      this.updateForm(transactionType);

      this.documentHistoryService
        .query({ 'transactionTypeId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!transactionType.history || !transactionType.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(transactionType.history.id)
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

  updateForm(transactionType: ITransactionType): void {
    this.editForm.patchValue({
      id: transactionType.id,
      transactionTypeCode: transactionType.transactionTypeCode,
      transactionType: transactionType.transactionType,
      isActive: transactionType.isActive,
      history: transactionType.history
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transactionType = this.createFromForm();
    if (transactionType.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionTypeService.update(transactionType));
    } else {
      this.subscribeToSaveResponse(this.transactionTypeService.create(transactionType));
    }
  }

  private createFromForm(): ITransactionType {
    return {
      ...new TransactionType(),
      id: this.editForm.get(['id'])!.value,
      transactionTypeCode: this.editForm.get(['transactionTypeCode'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      history: this.editForm.get(['history'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransactionType>>): void {
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

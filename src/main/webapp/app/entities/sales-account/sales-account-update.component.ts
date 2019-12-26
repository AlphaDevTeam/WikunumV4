import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { ISalesAccount, SalesAccount } from 'app/shared/model/sales-account.model';
import { SalesAccountService } from './sales-account.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';

type SelectableEntity = IDocumentHistory | ILocation | ITransactionType;

@Component({
  selector: 'jhi-sales-account-update',
  templateUrl: './sales-account-update.component.html'
})
export class SalesAccountUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  locations: ILocation[] = [];

  transactiontypes: ITransactionType[] = [];
  transactionDateDp: any;

  editForm = this.fb.group({
    id: [],
    transactionDate: [null, [Validators.required]],
    transactionDescription: [null, [Validators.required]],
    transactionAmountDR: [null, [Validators.required]],
    transactionAmountCR: [null, [Validators.required]],
    transactionBalance: [null, [Validators.required]],
    history: [],
    location: [null, Validators.required],
    transactionType: [null, Validators.required]
  });

  constructor(
    protected salesAccountService: SalesAccountService,
    protected documentHistoryService: DocumentHistoryService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salesAccount }) => {
      this.updateForm(salesAccount);

      this.documentHistoryService
        .query({ 'salesAccountId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!salesAccount.history || !salesAccount.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(salesAccount.history.id)
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

      this.locationService
        .query()
        .pipe(
          map((res: HttpResponse<ILocation[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ILocation[]) => (this.locations = resBody));

      this.transactionTypeService
        .query()
        .pipe(
          map((res: HttpResponse<ITransactionType[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ITransactionType[]) => (this.transactiontypes = resBody));
    });
  }

  updateForm(salesAccount: ISalesAccount): void {
    this.editForm.patchValue({
      id: salesAccount.id,
      transactionDate: salesAccount.transactionDate,
      transactionDescription: salesAccount.transactionDescription,
      transactionAmountDR: salesAccount.transactionAmountDR,
      transactionAmountCR: salesAccount.transactionAmountCR,
      transactionBalance: salesAccount.transactionBalance,
      history: salesAccount.history,
      location: salesAccount.location,
      transactionType: salesAccount.transactionType
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const salesAccount = this.createFromForm();
    if (salesAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.salesAccountService.update(salesAccount));
    } else {
      this.subscribeToSaveResponse(this.salesAccountService.create(salesAccount));
    }
  }

  private createFromForm(): ISalesAccount {
    return {
      ...new SalesAccount(),
      id: this.editForm.get(['id'])!.value,
      transactionDate: this.editForm.get(['transactionDate'])!.value,
      transactionDescription: this.editForm.get(['transactionDescription'])!.value,
      transactionAmountDR: this.editForm.get(['transactionAmountDR'])!.value,
      transactionAmountCR: this.editForm.get(['transactionAmountCR'])!.value,
      transactionBalance: this.editForm.get(['transactionBalance'])!.value,
      history: this.editForm.get(['history'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISalesAccount>>): void {
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

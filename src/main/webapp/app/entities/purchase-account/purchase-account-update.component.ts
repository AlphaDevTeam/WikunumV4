import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { IPurchaseAccount, PurchaseAccount } from 'app/shared/model/purchase-account.model';
import { PurchaseAccountService } from './purchase-account.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';

type SelectableEntity = IDocumentHistory | ILocation | ITransactionType;

@Component({
  selector: 'jhi-purchase-account-update',
  templateUrl: './purchase-account-update.component.html'
})
export class PurchaseAccountUpdateComponent implements OnInit {
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
    protected purchaseAccountService: PurchaseAccountService,
    protected documentHistoryService: DocumentHistoryService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaseAccount }) => {
      this.updateForm(purchaseAccount);

      this.documentHistoryService
        .query({ 'purchaseAccountId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!purchaseAccount.history || !purchaseAccount.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(purchaseAccount.history.id)
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

  updateForm(purchaseAccount: IPurchaseAccount): void {
    this.editForm.patchValue({
      id: purchaseAccount.id,
      transactionDate: purchaseAccount.transactionDate,
      transactionDescription: purchaseAccount.transactionDescription,
      transactionAmountDR: purchaseAccount.transactionAmountDR,
      transactionAmountCR: purchaseAccount.transactionAmountCR,
      transactionBalance: purchaseAccount.transactionBalance,
      history: purchaseAccount.history,
      location: purchaseAccount.location,
      transactionType: purchaseAccount.transactionType
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const purchaseAccount = this.createFromForm();
    if (purchaseAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.purchaseAccountService.update(purchaseAccount));
    } else {
      this.subscribeToSaveResponse(this.purchaseAccountService.create(purchaseAccount));
    }
  }

  private createFromForm(): IPurchaseAccount {
    return {
      ...new PurchaseAccount(),
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaseAccount>>): void {
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

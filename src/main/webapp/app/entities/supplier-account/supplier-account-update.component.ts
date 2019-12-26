import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { ISupplierAccount, SupplierAccount } from 'app/shared/model/supplier-account.model';
import { SupplierAccountService } from './supplier-account.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService } from 'app/entities/supplier/supplier.service';

type SelectableEntity = IDocumentHistory | ILocation | ITransactionType | ISupplier;

@Component({
  selector: 'jhi-supplier-account-update',
  templateUrl: './supplier-account-update.component.html'
})
export class SupplierAccountUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  locations: ILocation[] = [];

  transactiontypes: ITransactionType[] = [];

  suppliers: ISupplier[] = [];
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
    transactionType: [null, Validators.required],
    supplier: [null, Validators.required]
  });

  constructor(
    protected supplierAccountService: SupplierAccountService,
    protected documentHistoryService: DocumentHistoryService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected supplierService: SupplierService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ supplierAccount }) => {
      this.updateForm(supplierAccount);

      this.documentHistoryService
        .query({ 'supplierAccountId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!supplierAccount.history || !supplierAccount.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(supplierAccount.history.id)
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

      this.supplierService
        .query()
        .pipe(
          map((res: HttpResponse<ISupplier[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ISupplier[]) => (this.suppliers = resBody));
    });
  }

  updateForm(supplierAccount: ISupplierAccount): void {
    this.editForm.patchValue({
      id: supplierAccount.id,
      transactionDate: supplierAccount.transactionDate,
      transactionDescription: supplierAccount.transactionDescription,
      transactionAmountDR: supplierAccount.transactionAmountDR,
      transactionAmountCR: supplierAccount.transactionAmountCR,
      transactionBalance: supplierAccount.transactionBalance,
      history: supplierAccount.history,
      location: supplierAccount.location,
      transactionType: supplierAccount.transactionType,
      supplier: supplierAccount.supplier
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const supplierAccount = this.createFromForm();
    if (supplierAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.supplierAccountService.update(supplierAccount));
    } else {
      this.subscribeToSaveResponse(this.supplierAccountService.create(supplierAccount));
    }
  }

  private createFromForm(): ISupplierAccount {
    return {
      ...new SupplierAccount(),
      id: this.editForm.get(['id'])!.value,
      transactionDate: this.editForm.get(['transactionDate'])!.value,
      transactionDescription: this.editForm.get(['transactionDescription'])!.value,
      transactionAmountDR: this.editForm.get(['transactionAmountDR'])!.value,
      transactionAmountCR: this.editForm.get(['transactionAmountCR'])!.value,
      transactionBalance: this.editForm.get(['transactionBalance'])!.value,
      history: this.editForm.get(['history'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      supplier: this.editForm.get(['supplier'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISupplierAccount>>): void {
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

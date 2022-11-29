import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { ICostOfSalesAccount, CostOfSalesAccount } from 'app/shared/model/cost-of-sales-account.model';
import { CostOfSalesAccountService } from './cost-of-sales-account.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';

type SelectableEntity = ILocation | ITransactionType;

@Component({
  selector: 'jhi-cost-of-sales-account-update',
  templateUrl: './cost-of-sales-account-update.component.html'
})
export class CostOfSalesAccountUpdateComponent implements OnInit {
  isSaving = false;

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
    location: [null, Validators.required],
    transactionType: [null, Validators.required]
  });

  constructor(
    protected costOfSalesAccountService: CostOfSalesAccountService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ costOfSalesAccount }) => {
      this.updateForm(costOfSalesAccount);

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

  updateForm(costOfSalesAccount: ICostOfSalesAccount): void {
    this.editForm.patchValue({
      id: costOfSalesAccount.id,
      transactionDate: costOfSalesAccount.transactionDate,
      transactionDescription: costOfSalesAccount.transactionDescription,
      transactionAmountDR: costOfSalesAccount.transactionAmountDR,
      transactionAmountCR: costOfSalesAccount.transactionAmountCR,
      transactionBalance: costOfSalesAccount.transactionBalance,
      location: costOfSalesAccount.location,
      transactionType: costOfSalesAccount.transactionType
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const costOfSalesAccount = this.createFromForm();
    if (costOfSalesAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.costOfSalesAccountService.update(costOfSalesAccount));
    } else {
      this.subscribeToSaveResponse(this.costOfSalesAccountService.create(costOfSalesAccount));
    }
  }

  private createFromForm(): ICostOfSalesAccount {
    return {
      ...new CostOfSalesAccount(),
      id: this.editForm.get(['id'])!.value,
      transactionDate: this.editForm.get(['transactionDate'])!.value,
      transactionDescription: this.editForm.get(['transactionDescription'])!.value,
      transactionAmountDR: this.editForm.get(['transactionAmountDR'])!.value,
      transactionAmountCR: this.editForm.get(['transactionAmountCR'])!.value,
      transactionBalance: this.editForm.get(['transactionBalance'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICostOfSalesAccount>>): void {
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

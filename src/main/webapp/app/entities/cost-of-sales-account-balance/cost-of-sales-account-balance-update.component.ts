import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ICostOfSalesAccountBalance, CostOfSalesAccountBalance } from 'app/shared/model/cost-of-sales-account-balance.model';
import { CostOfSalesAccountBalanceService } from './cost-of-sales-account-balance.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';

type SelectableEntity = ILocation | ITransactionType;

@Component({
  selector: 'jhi-cost-of-sales-account-balance-update',
  templateUrl: './cost-of-sales-account-balance-update.component.html'
})
export class CostOfSalesAccountBalanceUpdateComponent implements OnInit {
  isSaving = false;

  locations: ILocation[] = [];

  transactiontypes: ITransactionType[] = [];

  editForm = this.fb.group({
    id: [],
    balance: [null, [Validators.required]],
    location: [null, Validators.required],
    transactionType: [null, Validators.required]
  });

  constructor(
    protected costOfSalesAccountBalanceService: CostOfSalesAccountBalanceService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ costOfSalesAccountBalance }) => {
      this.updateForm(costOfSalesAccountBalance);

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

  updateForm(costOfSalesAccountBalance: ICostOfSalesAccountBalance): void {
    this.editForm.patchValue({
      id: costOfSalesAccountBalance.id,
      balance: costOfSalesAccountBalance.balance,
      location: costOfSalesAccountBalance.location,
      transactionType: costOfSalesAccountBalance.transactionType
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const costOfSalesAccountBalance = this.createFromForm();
    if (costOfSalesAccountBalance.id !== undefined) {
      this.subscribeToSaveResponse(this.costOfSalesAccountBalanceService.update(costOfSalesAccountBalance));
    } else {
      this.subscribeToSaveResponse(this.costOfSalesAccountBalanceService.create(costOfSalesAccountBalance));
    }
  }

  private createFromForm(): ICostOfSalesAccountBalance {
    return {
      ...new CostOfSalesAccountBalance(),
      id: this.editForm.get(['id'])!.value,
      balance: this.editForm.get(['balance'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICostOfSalesAccountBalance>>): void {
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

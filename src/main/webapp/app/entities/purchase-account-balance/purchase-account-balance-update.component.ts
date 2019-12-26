import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IPurchaseAccountBalance, PurchaseAccountBalance } from 'app/shared/model/purchase-account-balance.model';
import { PurchaseAccountBalanceService } from './purchase-account-balance.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';

type SelectableEntity = ILocation | ITransactionType;

@Component({
  selector: 'jhi-purchase-account-balance-update',
  templateUrl: './purchase-account-balance-update.component.html'
})
export class PurchaseAccountBalanceUpdateComponent implements OnInit {
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
    protected purchaseAccountBalanceService: PurchaseAccountBalanceService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaseAccountBalance }) => {
      this.updateForm(purchaseAccountBalance);

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

  updateForm(purchaseAccountBalance: IPurchaseAccountBalance): void {
    this.editForm.patchValue({
      id: purchaseAccountBalance.id,
      balance: purchaseAccountBalance.balance,
      location: purchaseAccountBalance.location,
      transactionType: purchaseAccountBalance.transactionType
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const purchaseAccountBalance = this.createFromForm();
    if (purchaseAccountBalance.id !== undefined) {
      this.subscribeToSaveResponse(this.purchaseAccountBalanceService.update(purchaseAccountBalance));
    } else {
      this.subscribeToSaveResponse(this.purchaseAccountBalanceService.create(purchaseAccountBalance));
    }
  }

  private createFromForm(): IPurchaseAccountBalance {
    return {
      ...new PurchaseAccountBalance(),
      id: this.editForm.get(['id'])!.value,
      balance: this.editForm.get(['balance'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaseAccountBalance>>): void {
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

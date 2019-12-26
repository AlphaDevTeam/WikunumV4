import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ISupplierAccountBalance, SupplierAccountBalance } from 'app/shared/model/supplier-account-balance.model';
import { SupplierAccountBalanceService } from './supplier-account-balance.service';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService } from 'app/entities/supplier/supplier.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';

type SelectableEntity = ISupplier | ILocation | ITransactionType;

@Component({
  selector: 'jhi-supplier-account-balance-update',
  templateUrl: './supplier-account-balance-update.component.html'
})
export class SupplierAccountBalanceUpdateComponent implements OnInit {
  isSaving = false;

  suppliers: ISupplier[] = [];

  locations: ILocation[] = [];

  transactiontypes: ITransactionType[] = [];

  editForm = this.fb.group({
    id: [],
    balance: [null, [Validators.required]],
    supplier: [null, Validators.required],
    location: [null, Validators.required],
    transactionType: [null, Validators.required]
  });

  constructor(
    protected supplierAccountBalanceService: SupplierAccountBalanceService,
    protected supplierService: SupplierService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ supplierAccountBalance }) => {
      this.updateForm(supplierAccountBalance);

      this.supplierService
        .query()
        .pipe(
          map((res: HttpResponse<ISupplier[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ISupplier[]) => (this.suppliers = resBody));

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

  updateForm(supplierAccountBalance: ISupplierAccountBalance): void {
    this.editForm.patchValue({
      id: supplierAccountBalance.id,
      balance: supplierAccountBalance.balance,
      supplier: supplierAccountBalance.supplier,
      location: supplierAccountBalance.location,
      transactionType: supplierAccountBalance.transactionType
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const supplierAccountBalance = this.createFromForm();
    if (supplierAccountBalance.id !== undefined) {
      this.subscribeToSaveResponse(this.supplierAccountBalanceService.update(supplierAccountBalance));
    } else {
      this.subscribeToSaveResponse(this.supplierAccountBalanceService.create(supplierAccountBalance));
    }
  }

  private createFromForm(): ISupplierAccountBalance {
    return {
      ...new SupplierAccountBalance(),
      id: this.editForm.get(['id'])!.value,
      balance: this.editForm.get(['balance'])!.value,
      supplier: this.editForm.get(['supplier'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISupplierAccountBalance>>): void {
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

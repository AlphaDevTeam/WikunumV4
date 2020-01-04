import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { IEmployeeAccount, EmployeeAccount } from 'app/shared/model/employee-account.model';
import { EmployeeAccountService } from './employee-account.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';
import { IEmployee } from 'app/shared/model/employee.model';
import { EmployeeService } from 'app/entities/employee/employee.service';

type SelectableEntity = ILocation | ITransactionType | IEmployee;

@Component({
  selector: 'jhi-employee-account-update',
  templateUrl: './employee-account-update.component.html'
})
export class EmployeeAccountUpdateComponent implements OnInit {
  isSaving = false;

  locations: ILocation[] = [];

  transactiontypes: ITransactionType[] = [];

  employees: IEmployee[] = [];
  transactionDateDp: any;

  editForm = this.fb.group({
    id: [],
    transactionDate: [null, [Validators.required]],
    transactionDescription: [null, [Validators.required]],
    transactionAmountDR: [null, [Validators.required]],
    transactionAmountCR: [null, [Validators.required]],
    transactionBalance: [null, [Validators.required]],
    location: [null, Validators.required],
    transactionType: [null, Validators.required],
    employee: [null, Validators.required]
  });

  constructor(
    protected employeeAccountService: EmployeeAccountService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeAccount }) => {
      this.updateForm(employeeAccount);

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

      this.employeeService
        .query()
        .pipe(
          map((res: HttpResponse<IEmployee[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IEmployee[]) => (this.employees = resBody));
    });
  }

  updateForm(employeeAccount: IEmployeeAccount): void {
    this.editForm.patchValue({
      id: employeeAccount.id,
      transactionDate: employeeAccount.transactionDate,
      transactionDescription: employeeAccount.transactionDescription,
      transactionAmountDR: employeeAccount.transactionAmountDR,
      transactionAmountCR: employeeAccount.transactionAmountCR,
      transactionBalance: employeeAccount.transactionBalance,
      location: employeeAccount.location,
      transactionType: employeeAccount.transactionType,
      employee: employeeAccount.employee
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeeAccount = this.createFromForm();
    if (employeeAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeAccountService.update(employeeAccount));
    } else {
      this.subscribeToSaveResponse(this.employeeAccountService.create(employeeAccount));
    }
  }

  private createFromForm(): IEmployeeAccount {
    return {
      ...new EmployeeAccount(),
      id: this.editForm.get(['id'])!.value,
      transactionDate: this.editForm.get(['transactionDate'])!.value,
      transactionDescription: this.editForm.get(['transactionDescription'])!.value,
      transactionAmountDR: this.editForm.get(['transactionAmountDR'])!.value,
      transactionAmountCR: this.editForm.get(['transactionAmountCR'])!.value,
      transactionBalance: this.editForm.get(['transactionBalance'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      employee: this.editForm.get(['employee'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeeAccount>>): void {
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

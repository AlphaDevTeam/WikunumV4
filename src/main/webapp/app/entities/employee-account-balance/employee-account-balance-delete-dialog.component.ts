import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEmployeeAccountBalance } from 'app/shared/model/employee-account-balance.model';
import { EmployeeAccountBalanceService } from './employee-account-balance.service';

@Component({
  templateUrl: './employee-account-balance-delete-dialog.component.html'
})
export class EmployeeAccountBalanceDeleteDialogComponent {
  employeeAccountBalance?: IEmployeeAccountBalance;

  constructor(
    protected employeeAccountBalanceService: EmployeeAccountBalanceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeAccountBalanceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('employeeAccountBalanceListModification');
      this.activeModal.close();
    });
  }
}

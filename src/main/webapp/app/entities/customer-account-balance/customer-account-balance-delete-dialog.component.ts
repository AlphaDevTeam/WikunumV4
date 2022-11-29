import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICustomerAccountBalance } from 'app/shared/model/customer-account-balance.model';
import { CustomerAccountBalanceService } from './customer-account-balance.service';

@Component({
  templateUrl: './customer-account-balance-delete-dialog.component.html'
})
export class CustomerAccountBalanceDeleteDialogComponent {
  customerAccountBalance?: ICustomerAccountBalance;

  constructor(
    protected customerAccountBalanceService: CustomerAccountBalanceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.customerAccountBalanceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('customerAccountBalanceListModification');
      this.activeModal.close();
    });
  }
}

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICostOfSalesAccountBalance } from 'app/shared/model/cost-of-sales-account-balance.model';
import { CostOfSalesAccountBalanceService } from './cost-of-sales-account-balance.service';

@Component({
  templateUrl: './cost-of-sales-account-balance-delete-dialog.component.html'
})
export class CostOfSalesAccountBalanceDeleteDialogComponent {
  costOfSalesAccountBalance?: ICostOfSalesAccountBalance;

  constructor(
    protected costOfSalesAccountBalanceService: CostOfSalesAccountBalanceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.costOfSalesAccountBalanceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('costOfSalesAccountBalanceListModification');
      this.activeModal.close();
    });
  }
}

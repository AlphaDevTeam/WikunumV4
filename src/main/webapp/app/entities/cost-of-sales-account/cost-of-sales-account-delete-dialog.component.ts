import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICostOfSalesAccount } from 'app/shared/model/cost-of-sales-account.model';
import { CostOfSalesAccountService } from './cost-of-sales-account.service';

@Component({
  templateUrl: './cost-of-sales-account-delete-dialog.component.html'
})
export class CostOfSalesAccountDeleteDialogComponent {
  costOfSalesAccount?: ICostOfSalesAccount;

  constructor(
    protected costOfSalesAccountService: CostOfSalesAccountService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.costOfSalesAccountService.delete(id).subscribe(() => {
      this.eventManager.broadcast('costOfSalesAccountListModification');
      this.activeModal.close();
    });
  }
}

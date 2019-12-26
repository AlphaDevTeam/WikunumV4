import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPurchaseAccountBalance } from 'app/shared/model/purchase-account-balance.model';
import { PurchaseAccountBalanceService } from './purchase-account-balance.service';

@Component({
  templateUrl: './purchase-account-balance-delete-dialog.component.html'
})
export class PurchaseAccountBalanceDeleteDialogComponent {
  purchaseAccountBalance?: IPurchaseAccountBalance;

  constructor(
    protected purchaseAccountBalanceService: PurchaseAccountBalanceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.purchaseAccountBalanceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('purchaseAccountBalanceListModification');
      this.activeModal.close();
    });
  }
}

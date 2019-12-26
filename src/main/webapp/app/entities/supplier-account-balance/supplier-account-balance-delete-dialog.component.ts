import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISupplierAccountBalance } from 'app/shared/model/supplier-account-balance.model';
import { SupplierAccountBalanceService } from './supplier-account-balance.service';

@Component({
  templateUrl: './supplier-account-balance-delete-dialog.component.html'
})
export class SupplierAccountBalanceDeleteDialogComponent {
  supplierAccountBalance?: ISupplierAccountBalance;

  constructor(
    protected supplierAccountBalanceService: SupplierAccountBalanceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.supplierAccountBalanceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('supplierAccountBalanceListModification');
      this.activeModal.close();
    });
  }
}

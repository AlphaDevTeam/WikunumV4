import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISupplierAccount } from 'app/shared/model/supplier-account.model';
import { SupplierAccountService } from './supplier-account.service';

@Component({
  templateUrl: './supplier-account-delete-dialog.component.html'
})
export class SupplierAccountDeleteDialogComponent {
  supplierAccount?: ISupplierAccount;

  constructor(
    protected supplierAccountService: SupplierAccountService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.supplierAccountService.delete(id).subscribe(() => {
      this.eventManager.broadcast('supplierAccountListModification');
      this.activeModal.close();
    });
  }
}

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICustomerAccount } from 'app/shared/model/customer-account.model';
import { CustomerAccountService } from './customer-account.service';

@Component({
  templateUrl: './customer-account-delete-dialog.component.html'
})
export class CustomerAccountDeleteDialogComponent {
  customerAccount?: ICustomerAccount;

  constructor(
    protected customerAccountService: CustomerAccountService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.customerAccountService.delete(id).subscribe(() => {
      this.eventManager.broadcast('customerAccountListModification');
      this.activeModal.close();
    });
  }
}

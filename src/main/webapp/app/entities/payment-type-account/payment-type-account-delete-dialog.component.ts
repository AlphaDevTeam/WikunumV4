import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPaymentTypeAccount } from 'app/shared/model/payment-type-account.model';
import { PaymentTypeAccountService } from './payment-type-account.service';

@Component({
  templateUrl: './payment-type-account-delete-dialog.component.html'
})
export class PaymentTypeAccountDeleteDialogComponent {
  paymentTypeAccount?: IPaymentTypeAccount;

  constructor(
    protected paymentTypeAccountService: PaymentTypeAccountService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paymentTypeAccountService.delete(id).subscribe(() => {
      this.eventManager.broadcast('paymentTypeAccountListModification');
      this.activeModal.close();
    });
  }
}

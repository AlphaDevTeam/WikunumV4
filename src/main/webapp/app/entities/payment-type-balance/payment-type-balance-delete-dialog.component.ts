import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPaymentTypeBalance } from 'app/shared/model/payment-type-balance.model';
import { PaymentTypeBalanceService } from './payment-type-balance.service';

@Component({
  templateUrl: './payment-type-balance-delete-dialog.component.html'
})
export class PaymentTypeBalanceDeleteDialogComponent {
  paymentTypeBalance?: IPaymentTypeBalance;

  constructor(
    protected paymentTypeBalanceService: PaymentTypeBalanceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paymentTypeBalanceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('paymentTypeBalanceListModification');
      this.activeModal.close();
    });
  }
}

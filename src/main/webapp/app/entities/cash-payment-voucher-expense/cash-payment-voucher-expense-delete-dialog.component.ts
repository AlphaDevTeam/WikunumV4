import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICashPaymentVoucherExpense } from 'app/shared/model/cash-payment-voucher-expense.model';
import { CashPaymentVoucherExpenseService } from './cash-payment-voucher-expense.service';

@Component({
  templateUrl: './cash-payment-voucher-expense-delete-dialog.component.html'
})
export class CashPaymentVoucherExpenseDeleteDialogComponent {
  cashPaymentVoucherExpense?: ICashPaymentVoucherExpense;

  constructor(
    protected cashPaymentVoucherExpenseService: CashPaymentVoucherExpenseService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cashPaymentVoucherExpenseService.delete(id).subscribe(() => {
      this.eventManager.broadcast('cashPaymentVoucherExpenseListModification');
      this.activeModal.close();
    });
  }
}

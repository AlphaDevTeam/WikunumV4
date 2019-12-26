import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICashPaymentVoucherSupplier } from 'app/shared/model/cash-payment-voucher-supplier.model';
import { CashPaymentVoucherSupplierService } from './cash-payment-voucher-supplier.service';

@Component({
  templateUrl: './cash-payment-voucher-supplier-delete-dialog.component.html'
})
export class CashPaymentVoucherSupplierDeleteDialogComponent {
  cashPaymentVoucherSupplier?: ICashPaymentVoucherSupplier;

  constructor(
    protected cashPaymentVoucherSupplierService: CashPaymentVoucherSupplierService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cashPaymentVoucherSupplierService.delete(id).subscribe(() => {
      this.eventManager.broadcast('cashPaymentVoucherSupplierListModification');
      this.activeModal.close();
    });
  }
}

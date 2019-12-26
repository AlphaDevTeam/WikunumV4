import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPaymentTypes } from 'app/shared/model/payment-types.model';
import { PaymentTypesService } from './payment-types.service';

@Component({
  templateUrl: './payment-types-delete-dialog.component.html'
})
export class PaymentTypesDeleteDialogComponent {
  paymentTypes?: IPaymentTypes;

  constructor(
    protected paymentTypesService: PaymentTypesService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paymentTypesService.delete(id).subscribe(() => {
      this.eventManager.broadcast('paymentTypesListModification');
      this.activeModal.close();
    });
  }
}

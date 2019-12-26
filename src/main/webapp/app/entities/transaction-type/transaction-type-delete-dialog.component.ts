import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from './transaction-type.service';

@Component({
  templateUrl: './transaction-type-delete-dialog.component.html'
})
export class TransactionTypeDeleteDialogComponent {
  transactionType?: ITransactionType;

  constructor(
    protected transactionTypeService: TransactionTypeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transactionTypeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('transactionTypeListModification');
      this.activeModal.close();
    });
  }
}

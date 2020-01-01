import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStockTransfer } from 'app/shared/model/stock-transfer.model';
import { StockTransferService } from './stock-transfer.service';

@Component({
  templateUrl: './stock-transfer-delete-dialog.component.html'
})
export class StockTransferDeleteDialogComponent {
  stockTransfer?: IStockTransfer;

  constructor(
    protected stockTransferService: StockTransferService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.stockTransferService.delete(id).subscribe(() => {
      this.eventManager.broadcast('stockTransferListModification');
      this.activeModal.close();
    });
  }
}

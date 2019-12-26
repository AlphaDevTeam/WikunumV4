import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGoodsReceipt } from 'app/shared/model/goods-receipt.model';
import { GoodsReceiptService } from './goods-receipt.service';

@Component({
  templateUrl: './goods-receipt-delete-dialog.component.html'
})
export class GoodsReceiptDeleteDialogComponent {
  goodsReceipt?: IGoodsReceipt;

  constructor(
    protected goodsReceiptService: GoodsReceiptService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.goodsReceiptService.delete(id).subscribe(() => {
      this.eventManager.broadcast('goodsReceiptListModification');
      this.activeModal.close();
    });
  }
}

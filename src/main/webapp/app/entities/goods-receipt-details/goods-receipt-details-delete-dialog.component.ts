import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGoodsReceiptDetails } from 'app/shared/model/goods-receipt-details.model';
import { GoodsReceiptDetailsService } from './goods-receipt-details.service';

@Component({
  templateUrl: './goods-receipt-details-delete-dialog.component.html'
})
export class GoodsReceiptDetailsDeleteDialogComponent {
  goodsReceiptDetails?: IGoodsReceiptDetails;

  constructor(
    protected goodsReceiptDetailsService: GoodsReceiptDetailsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.goodsReceiptDetailsService.delete(id).subscribe(() => {
      this.eventManager.broadcast('goodsReceiptDetailsListModification');
      this.activeModal.close();
    });
  }
}

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IItemBinCard } from 'app/shared/model/item-bin-card.model';
import { ItemBinCardService } from './item-bin-card.service';

@Component({
  templateUrl: './item-bin-card-delete-dialog.component.html'
})
export class ItemBinCardDeleteDialogComponent {
  itemBinCard?: IItemBinCard;

  constructor(
    protected itemBinCardService: ItemBinCardService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.itemBinCardService.delete(id).subscribe(() => {
      this.eventManager.broadcast('itemBinCardListModification');
      this.activeModal.close();
    });
  }
}

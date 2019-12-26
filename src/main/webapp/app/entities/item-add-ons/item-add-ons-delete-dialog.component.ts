import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IItemAddOns } from 'app/shared/model/item-add-ons.model';
import { ItemAddOnsService } from './item-add-ons.service';

@Component({
  templateUrl: './item-add-ons-delete-dialog.component.html'
})
export class ItemAddOnsDeleteDialogComponent {
  itemAddOns?: IItemAddOns;

  constructor(
    protected itemAddOnsService: ItemAddOnsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.itemAddOnsService.delete(id).subscribe(() => {
      this.eventManager.broadcast('itemAddOnsListModification');
      this.activeModal.close();
    });
  }
}

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMenuItems } from 'app/shared/model/menu-items.model';
import { MenuItemsService } from './menu-items.service';

@Component({
  templateUrl: './menu-items-delete-dialog.component.html'
})
export class MenuItemsDeleteDialogComponent {
  menuItems?: IMenuItems;

  constructor(protected menuItemsService: MenuItemsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.menuItemsService.delete(id).subscribe(() => {
      this.eventManager.broadcast('menuItemsListModification');
      this.activeModal.close();
    });
  }
}

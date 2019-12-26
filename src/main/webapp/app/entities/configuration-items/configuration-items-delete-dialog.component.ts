import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IConfigurationItems } from 'app/shared/model/configuration-items.model';
import { ConfigurationItemsService } from './configuration-items.service';

@Component({
  templateUrl: './configuration-items-delete-dialog.component.html'
})
export class ConfigurationItemsDeleteDialogComponent {
  configurationItems?: IConfigurationItems;

  constructor(
    protected configurationItemsService: ConfigurationItemsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.configurationItemsService.delete(id).subscribe(() => {
      this.eventManager.broadcast('configurationItemsListModification');
      this.activeModal.close();
    });
  }
}

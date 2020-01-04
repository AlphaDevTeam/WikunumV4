import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStorageBin } from 'app/shared/model/storage-bin.model';
import { StorageBinService } from './storage-bin.service';

@Component({
  templateUrl: './storage-bin-delete-dialog.component.html'
})
export class StorageBinDeleteDialogComponent {
  storageBin?: IStorageBin;

  constructor(
    protected storageBinService: StorageBinService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.storageBinService.delete(id).subscribe(() => {
      this.eventManager.broadcast('storageBinListModification');
      this.activeModal.close();
    });
  }
}

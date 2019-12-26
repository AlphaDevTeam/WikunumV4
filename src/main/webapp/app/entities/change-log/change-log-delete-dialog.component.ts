import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IChangeLog } from 'app/shared/model/change-log.model';
import { ChangeLogService } from './change-log.service';

@Component({
  templateUrl: './change-log-delete-dialog.component.html'
})
export class ChangeLogDeleteDialogComponent {
  changeLog?: IChangeLog;

  constructor(protected changeLogService: ChangeLogService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.changeLogService.delete(id).subscribe(() => {
      this.eventManager.broadcast('changeLogListModification');
      this.activeModal.close();
    });
  }
}

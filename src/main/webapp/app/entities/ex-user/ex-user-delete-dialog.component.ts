import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IExUser } from 'app/shared/model/ex-user.model';
import { ExUserService } from './ex-user.service';

@Component({
  templateUrl: './ex-user-delete-dialog.component.html'
})
export class ExUserDeleteDialogComponent {
  exUser?: IExUser;

  constructor(protected exUserService: ExUserService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.exUserService.delete(id).subscribe(() => {
      this.eventManager.broadcast('exUserListModification');
      this.activeModal.close();
    });
  }
}

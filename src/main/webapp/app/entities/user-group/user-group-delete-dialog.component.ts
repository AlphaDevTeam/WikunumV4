import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserGroup } from 'app/shared/model/user-group.model';
import { UserGroupService } from './user-group.service';

@Component({
  templateUrl: './user-group-delete-dialog.component.html'
})
export class UserGroupDeleteDialogComponent {
  userGroup?: IUserGroup;

  constructor(protected userGroupService: UserGroupService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userGroupService.delete(id).subscribe(() => {
      this.eventManager.broadcast('userGroupListModification');
      this.activeModal.close();
    });
  }
}

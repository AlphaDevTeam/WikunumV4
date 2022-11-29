import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IJobStatus } from 'app/shared/model/job-status.model';
import { JobStatusService } from './job-status.service';

@Component({
  templateUrl: './job-status-delete-dialog.component.html'
})
export class JobStatusDeleteDialogComponent {
  jobStatus?: IJobStatus;

  constructor(protected jobStatusService: JobStatusService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.jobStatusService.delete(id).subscribe(() => {
      this.eventManager.broadcast('jobStatusListModification');
      this.activeModal.close();
    });
  }
}

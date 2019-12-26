import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWorker } from 'app/shared/model/worker.model';
import { WorkerService } from './worker.service';

@Component({
  templateUrl: './worker-delete-dialog.component.html'
})
export class WorkerDeleteDialogComponent {
  worker?: IWorker;

  constructor(protected workerService: WorkerService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workerService.delete(id).subscribe(() => {
      this.eventManager.broadcast('workerListModification');
      this.activeModal.close();
    });
  }
}

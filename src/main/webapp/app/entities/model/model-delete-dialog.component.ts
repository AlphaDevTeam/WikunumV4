import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IModel } from 'app/shared/model/model.model';
import { ModelService } from './model.service';

@Component({
  templateUrl: './model-delete-dialog.component.html'
})
export class ModelDeleteDialogComponent {
  model?: IModel;

  constructor(protected modelService: ModelService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.modelService.delete(id).subscribe(() => {
      this.eventManager.broadcast('modelListModification');
      this.activeModal.close();
    });
  }
}

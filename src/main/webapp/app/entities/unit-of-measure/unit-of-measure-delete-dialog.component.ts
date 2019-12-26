import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUnitOfMeasure } from 'app/shared/model/unit-of-measure.model';
import { UnitOfMeasureService } from './unit-of-measure.service';

@Component({
  templateUrl: './unit-of-measure-delete-dialog.component.html'
})
export class UnitOfMeasureDeleteDialogComponent {
  unitOfMeasure?: IUnitOfMeasure;

  constructor(
    protected unitOfMeasureService: UnitOfMeasureService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.unitOfMeasureService.delete(id).subscribe(() => {
      this.eventManager.broadcast('unitOfMeasureListModification');
      this.activeModal.close();
    });
  }
}

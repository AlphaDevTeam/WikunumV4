import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILicenseType } from 'app/shared/model/license-type.model';
import { LicenseTypeService } from './license-type.service';

@Component({
  templateUrl: './license-type-delete-dialog.component.html'
})
export class LicenseTypeDeleteDialogComponent {
  licenseType?: ILicenseType;

  constructor(
    protected licenseTypeService: LicenseTypeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.licenseTypeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('licenseTypeListModification');
      this.activeModal.close();
    });
  }
}

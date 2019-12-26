import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDocumentNumberConfig } from 'app/shared/model/document-number-config.model';
import { DocumentNumberConfigService } from './document-number-config.service';

@Component({
  templateUrl: './document-number-config-delete-dialog.component.html'
})
export class DocumentNumberConfigDeleteDialogComponent {
  documentNumberConfig?: IDocumentNumberConfig;

  constructor(
    protected documentNumberConfigService: DocumentNumberConfigService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentNumberConfigService.delete(id).subscribe(() => {
      this.eventManager.broadcast('documentNumberConfigListModification');
      this.activeModal.close();
    });
  }
}

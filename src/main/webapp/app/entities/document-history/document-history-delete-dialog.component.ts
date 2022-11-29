import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from './document-history.service';

@Component({
  templateUrl: './document-history-delete-dialog.component.html'
})
export class DocumentHistoryDeleteDialogComponent {
  documentHistory?: IDocumentHistory;

  constructor(
    protected documentHistoryService: DocumentHistoryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentHistoryService.delete(id).subscribe(() => {
      this.eventManager.broadcast('documentHistoryListModification');
      this.activeModal.close();
    });
  }
}

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IQuotation } from 'app/shared/model/quotation.model';
import { QuotationService } from './quotation.service';

@Component({
  templateUrl: './quotation-delete-dialog.component.html'
})
export class QuotationDeleteDialogComponent {
  quotation?: IQuotation;

  constructor(protected quotationService: QuotationService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.quotationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('quotationListModification');
      this.activeModal.close();
    });
  }
}

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IQuotationDetails } from 'app/shared/model/quotation-details.model';
import { QuotationDetailsService } from './quotation-details.service';

@Component({
  templateUrl: './quotation-details-delete-dialog.component.html'
})
export class QuotationDetailsDeleteDialogComponent {
  quotationDetails?: IQuotationDetails;

  constructor(
    protected quotationDetailsService: QuotationDetailsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.quotationDetailsService.delete(id).subscribe(() => {
      this.eventManager.broadcast('quotationDetailsListModification');
      this.activeModal.close();
    });
  }
}

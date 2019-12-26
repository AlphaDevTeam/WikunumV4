import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICashBook } from 'app/shared/model/cash-book.model';
import { CashBookService } from './cash-book.service';

@Component({
  templateUrl: './cash-book-delete-dialog.component.html'
})
export class CashBookDeleteDialogComponent {
  cashBook?: ICashBook;

  constructor(protected cashBookService: CashBookService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cashBookService.delete(id).subscribe(() => {
      this.eventManager.broadcast('cashBookListModification');
      this.activeModal.close();
    });
  }
}

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IExpenseAccount } from 'app/shared/model/expense-account.model';
import { ExpenseAccountService } from './expense-account.service';

@Component({
  templateUrl: './expense-account-delete-dialog.component.html'
})
export class ExpenseAccountDeleteDialogComponent {
  expenseAccount?: IExpenseAccount;

  constructor(
    protected expenseAccountService: ExpenseAccountService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.expenseAccountService.delete(id).subscribe(() => {
      this.eventManager.broadcast('expenseAccountListModification');
      this.activeModal.close();
    });
  }
}

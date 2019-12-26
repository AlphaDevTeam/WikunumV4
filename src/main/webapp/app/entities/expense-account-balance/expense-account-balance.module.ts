import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { ExpenseAccountBalanceComponent } from './expense-account-balance.component';
import { ExpenseAccountBalanceDetailComponent } from './expense-account-balance-detail.component';
import { ExpenseAccountBalanceUpdateComponent } from './expense-account-balance-update.component';
import { ExpenseAccountBalanceDeleteDialogComponent } from './expense-account-balance-delete-dialog.component';
import { expenseAccountBalanceRoute } from './expense-account-balance.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(expenseAccountBalanceRoute)],
  declarations: [
    ExpenseAccountBalanceComponent,
    ExpenseAccountBalanceDetailComponent,
    ExpenseAccountBalanceUpdateComponent,
    ExpenseAccountBalanceDeleteDialogComponent
  ],
  entryComponents: [ExpenseAccountBalanceDeleteDialogComponent]
})
export class WikunumExpenseAccountBalanceModule {}

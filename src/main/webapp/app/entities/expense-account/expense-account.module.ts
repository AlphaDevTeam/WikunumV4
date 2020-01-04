import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { ExpenseAccountComponent } from './expense-account.component';
import { ExpenseAccountDetailComponent } from './expense-account-detail.component';
import { ExpenseAccountUpdateComponent } from './expense-account-update.component';
import { ExpenseAccountDeleteDialogComponent } from './expense-account-delete-dialog.component';
import { expenseAccountRoute } from './expense-account.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(expenseAccountRoute)],
  declarations: [
    ExpenseAccountComponent,
    ExpenseAccountDetailComponent,
    ExpenseAccountUpdateComponent,
    ExpenseAccountDeleteDialogComponent
  ],
  entryComponents: [ExpenseAccountDeleteDialogComponent]
})
export class WikunumExpenseAccountModule {}

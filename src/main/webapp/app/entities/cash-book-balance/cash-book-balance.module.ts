import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { CashBookBalanceComponent } from './cash-book-balance.component';
import { CashBookBalanceDetailComponent } from './cash-book-balance-detail.component';
import { CashBookBalanceUpdateComponent } from './cash-book-balance-update.component';
import { CashBookBalanceDeleteDialogComponent } from './cash-book-balance-delete-dialog.component';
import { cashBookBalanceRoute } from './cash-book-balance.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(cashBookBalanceRoute)],
  declarations: [
    CashBookBalanceComponent,
    CashBookBalanceDetailComponent,
    CashBookBalanceUpdateComponent,
    CashBookBalanceDeleteDialogComponent
  ],
  entryComponents: [CashBookBalanceDeleteDialogComponent]
})
export class WikunumCashBookBalanceModule {}

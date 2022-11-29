import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { SalesAccountBalanceComponent } from './sales-account-balance.component';
import { SalesAccountBalanceDetailComponent } from './sales-account-balance-detail.component';
import { SalesAccountBalanceUpdateComponent } from './sales-account-balance-update.component';
import { SalesAccountBalanceDeleteDialogComponent } from './sales-account-balance-delete-dialog.component';
import { salesAccountBalanceRoute } from './sales-account-balance.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(salesAccountBalanceRoute)],
  declarations: [
    SalesAccountBalanceComponent,
    SalesAccountBalanceDetailComponent,
    SalesAccountBalanceUpdateComponent,
    SalesAccountBalanceDeleteDialogComponent
  ],
  entryComponents: [SalesAccountBalanceDeleteDialogComponent]
})
export class WikunumSalesAccountBalanceModule {}

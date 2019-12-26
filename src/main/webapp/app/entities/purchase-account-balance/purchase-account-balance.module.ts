import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { PurchaseAccountBalanceComponent } from './purchase-account-balance.component';
import { PurchaseAccountBalanceDetailComponent } from './purchase-account-balance-detail.component';
import { PurchaseAccountBalanceUpdateComponent } from './purchase-account-balance-update.component';
import { PurchaseAccountBalanceDeleteDialogComponent } from './purchase-account-balance-delete-dialog.component';
import { purchaseAccountBalanceRoute } from './purchase-account-balance.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(purchaseAccountBalanceRoute)],
  declarations: [
    PurchaseAccountBalanceComponent,
    PurchaseAccountBalanceDetailComponent,
    PurchaseAccountBalanceUpdateComponent,
    PurchaseAccountBalanceDeleteDialogComponent
  ],
  entryComponents: [PurchaseAccountBalanceDeleteDialogComponent]
})
export class WikunumPurchaseAccountBalanceModule {}

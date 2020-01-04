import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { CostOfSalesAccountBalanceComponent } from './cost-of-sales-account-balance.component';
import { CostOfSalesAccountBalanceDetailComponent } from './cost-of-sales-account-balance-detail.component';
import { CostOfSalesAccountBalanceUpdateComponent } from './cost-of-sales-account-balance-update.component';
import { CostOfSalesAccountBalanceDeleteDialogComponent } from './cost-of-sales-account-balance-delete-dialog.component';
import { costOfSalesAccountBalanceRoute } from './cost-of-sales-account-balance.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(costOfSalesAccountBalanceRoute)],
  declarations: [
    CostOfSalesAccountBalanceComponent,
    CostOfSalesAccountBalanceDetailComponent,
    CostOfSalesAccountBalanceUpdateComponent,
    CostOfSalesAccountBalanceDeleteDialogComponent
  ],
  entryComponents: [CostOfSalesAccountBalanceDeleteDialogComponent]
})
export class WikunumCostOfSalesAccountBalanceModule {}

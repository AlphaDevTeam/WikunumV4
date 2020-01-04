import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { CostOfSalesAccountComponent } from './cost-of-sales-account.component';
import { CostOfSalesAccountDetailComponent } from './cost-of-sales-account-detail.component';
import { CostOfSalesAccountUpdateComponent } from './cost-of-sales-account-update.component';
import { CostOfSalesAccountDeleteDialogComponent } from './cost-of-sales-account-delete-dialog.component';
import { costOfSalesAccountRoute } from './cost-of-sales-account.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(costOfSalesAccountRoute)],
  declarations: [
    CostOfSalesAccountComponent,
    CostOfSalesAccountDetailComponent,
    CostOfSalesAccountUpdateComponent,
    CostOfSalesAccountDeleteDialogComponent
  ],
  entryComponents: [CostOfSalesAccountDeleteDialogComponent]
})
export class WikunumCostOfSalesAccountModule {}

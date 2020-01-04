import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { CustomerAccountBalanceComponent } from './customer-account-balance.component';
import { CustomerAccountBalanceDetailComponent } from './customer-account-balance-detail.component';
import { CustomerAccountBalanceUpdateComponent } from './customer-account-balance-update.component';
import { CustomerAccountBalanceDeleteDialogComponent } from './customer-account-balance-delete-dialog.component';
import { customerAccountBalanceRoute } from './customer-account-balance.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(customerAccountBalanceRoute)],
  declarations: [
    CustomerAccountBalanceComponent,
    CustomerAccountBalanceDetailComponent,
    CustomerAccountBalanceUpdateComponent,
    CustomerAccountBalanceDeleteDialogComponent
  ],
  entryComponents: [CustomerAccountBalanceDeleteDialogComponent]
})
export class WikunumCustomerAccountBalanceModule {}

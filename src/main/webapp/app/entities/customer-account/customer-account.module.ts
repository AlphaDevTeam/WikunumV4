import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { CustomerAccountComponent } from './customer-account.component';
import { CustomerAccountDetailComponent } from './customer-account-detail.component';
import { CustomerAccountUpdateComponent } from './customer-account-update.component';
import { CustomerAccountDeleteDialogComponent } from './customer-account-delete-dialog.component';
import { customerAccountRoute } from './customer-account.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(customerAccountRoute)],
  declarations: [
    CustomerAccountComponent,
    CustomerAccountDetailComponent,
    CustomerAccountUpdateComponent,
    CustomerAccountDeleteDialogComponent
  ],
  entryComponents: [CustomerAccountDeleteDialogComponent]
})
export class WikunumCustomerAccountModule {}

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { SupplierAccountBalanceComponent } from './supplier-account-balance.component';
import { SupplierAccountBalanceDetailComponent } from './supplier-account-balance-detail.component';
import { SupplierAccountBalanceUpdateComponent } from './supplier-account-balance-update.component';
import { SupplierAccountBalanceDeleteDialogComponent } from './supplier-account-balance-delete-dialog.component';
import { supplierAccountBalanceRoute } from './supplier-account-balance.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(supplierAccountBalanceRoute)],
  declarations: [
    SupplierAccountBalanceComponent,
    SupplierAccountBalanceDetailComponent,
    SupplierAccountBalanceUpdateComponent,
    SupplierAccountBalanceDeleteDialogComponent
  ],
  entryComponents: [SupplierAccountBalanceDeleteDialogComponent]
})
export class WikunumSupplierAccountBalanceModule {}

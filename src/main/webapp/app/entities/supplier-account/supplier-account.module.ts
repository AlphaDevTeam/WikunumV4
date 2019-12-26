import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { SupplierAccountComponent } from './supplier-account.component';
import { SupplierAccountDetailComponent } from './supplier-account-detail.component';
import { SupplierAccountUpdateComponent } from './supplier-account-update.component';
import { SupplierAccountDeleteDialogComponent } from './supplier-account-delete-dialog.component';
import { supplierAccountRoute } from './supplier-account.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(supplierAccountRoute)],
  declarations: [
    SupplierAccountComponent,
    SupplierAccountDetailComponent,
    SupplierAccountUpdateComponent,
    SupplierAccountDeleteDialogComponent
  ],
  entryComponents: [SupplierAccountDeleteDialogComponent]
})
export class WikunumSupplierAccountModule {}

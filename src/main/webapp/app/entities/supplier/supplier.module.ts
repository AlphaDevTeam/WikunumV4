import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { SupplierComponent } from './supplier.component';
import { SupplierDetailComponent } from './supplier-detail.component';
import { SupplierUpdateComponent } from './supplier-update.component';
import { SupplierDeleteDialogComponent } from './supplier-delete-dialog.component';
import { supplierRoute } from './supplier.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(supplierRoute)],
  declarations: [SupplierComponent, SupplierDetailComponent, SupplierUpdateComponent, SupplierDeleteDialogComponent],
  entryComponents: [SupplierDeleteDialogComponent]
})
export class WikunumSupplierModule {}

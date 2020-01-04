import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { PurchaseOrderDetailsComponent } from './purchase-order-details.component';
import { PurchaseOrderDetailsDetailComponent } from './purchase-order-details-detail.component';
import { PurchaseOrderDetailsUpdateComponent } from './purchase-order-details-update.component';
import { PurchaseOrderDetailsDeleteDialogComponent } from './purchase-order-details-delete-dialog.component';
import { purchaseOrderDetailsRoute } from './purchase-order-details.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(purchaseOrderDetailsRoute)],
  declarations: [
    PurchaseOrderDetailsComponent,
    PurchaseOrderDetailsDetailComponent,
    PurchaseOrderDetailsUpdateComponent,
    PurchaseOrderDetailsDeleteDialogComponent
  ],
  entryComponents: [PurchaseOrderDetailsDeleteDialogComponent]
})
export class WikunumPurchaseOrderDetailsModule {}

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { PurchaseOrderComponent } from './purchase-order.component';
import { PurchaseOrderDetailComponent } from './purchase-order-detail.component';
import { PurchaseOrderUpdateComponent } from './purchase-order-update.component';
import { PurchaseOrderDeleteDialogComponent } from './purchase-order-delete-dialog.component';
import { purchaseOrderRoute } from './purchase-order.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(purchaseOrderRoute)],
  declarations: [PurchaseOrderComponent, PurchaseOrderDetailComponent, PurchaseOrderUpdateComponent, PurchaseOrderDeleteDialogComponent],
  entryComponents: [PurchaseOrderDeleteDialogComponent]
})
export class WikunumPurchaseOrderModule {}

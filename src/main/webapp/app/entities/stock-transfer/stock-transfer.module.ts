import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { StockTransferComponent } from './stock-transfer.component';
import { StockTransferDetailComponent } from './stock-transfer-detail.component';
import { StockTransferUpdateComponent } from './stock-transfer-update.component';
import { StockTransferDeleteDialogComponent } from './stock-transfer-delete-dialog.component';
import { stockTransferRoute } from './stock-transfer.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(stockTransferRoute)],
  declarations: [StockTransferComponent, StockTransferDetailComponent, StockTransferUpdateComponent, StockTransferDeleteDialogComponent],
  entryComponents: [StockTransferDeleteDialogComponent]
})
export class WikunumStockTransferModule {}

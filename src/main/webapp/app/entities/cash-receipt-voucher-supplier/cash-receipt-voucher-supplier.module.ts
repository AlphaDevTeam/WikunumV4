import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { CashReceiptVoucherSupplierComponent } from './cash-receipt-voucher-supplier.component';
import { CashReceiptVoucherSupplierDetailComponent } from './cash-receipt-voucher-supplier-detail.component';
import { CashReceiptVoucherSupplierUpdateComponent } from './cash-receipt-voucher-supplier-update.component';
import { CashReceiptVoucherSupplierDeleteDialogComponent } from './cash-receipt-voucher-supplier-delete-dialog.component';
import { cashReceiptVoucherSupplierRoute } from './cash-receipt-voucher-supplier.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(cashReceiptVoucherSupplierRoute)],
  declarations: [
    CashReceiptVoucherSupplierComponent,
    CashReceiptVoucherSupplierDetailComponent,
    CashReceiptVoucherSupplierUpdateComponent,
    CashReceiptVoucherSupplierDeleteDialogComponent
  ],
  entryComponents: [CashReceiptVoucherSupplierDeleteDialogComponent]
})
export class WikunumCashReceiptVoucherSupplierModule {}

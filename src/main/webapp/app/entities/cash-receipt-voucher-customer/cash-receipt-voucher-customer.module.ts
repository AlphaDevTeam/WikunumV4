import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { CashReceiptVoucherCustomerComponent } from './cash-receipt-voucher-customer.component';
import { CashReceiptVoucherCustomerDetailComponent } from './cash-receipt-voucher-customer-detail.component';
import { CashReceiptVoucherCustomerUpdateComponent } from './cash-receipt-voucher-customer-update.component';
import { CashReceiptVoucherCustomerDeleteDialogComponent } from './cash-receipt-voucher-customer-delete-dialog.component';
import { cashReceiptVoucherCustomerRoute } from './cash-receipt-voucher-customer.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(cashReceiptVoucherCustomerRoute)],
  declarations: [
    CashReceiptVoucherCustomerComponent,
    CashReceiptVoucherCustomerDetailComponent,
    CashReceiptVoucherCustomerUpdateComponent,
    CashReceiptVoucherCustomerDeleteDialogComponent
  ],
  entryComponents: [CashReceiptVoucherCustomerDeleteDialogComponent]
})
export class WikunumCashReceiptVoucherCustomerModule {}

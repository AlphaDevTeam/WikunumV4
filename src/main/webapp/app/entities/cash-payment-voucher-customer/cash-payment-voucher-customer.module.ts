import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { CashPaymentVoucherCustomerComponent } from './cash-payment-voucher-customer.component';
import { CashPaymentVoucherCustomerDetailComponent } from './cash-payment-voucher-customer-detail.component';
import { CashPaymentVoucherCustomerUpdateComponent } from './cash-payment-voucher-customer-update.component';
import { CashPaymentVoucherCustomerDeleteDialogComponent } from './cash-payment-voucher-customer-delete-dialog.component';
import { cashPaymentVoucherCustomerRoute } from './cash-payment-voucher-customer.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(cashPaymentVoucherCustomerRoute)],
  declarations: [
    CashPaymentVoucherCustomerComponent,
    CashPaymentVoucherCustomerDetailComponent,
    CashPaymentVoucherCustomerUpdateComponent,
    CashPaymentVoucherCustomerDeleteDialogComponent
  ],
  entryComponents: [CashPaymentVoucherCustomerDeleteDialogComponent]
})
export class WikunumCashPaymentVoucherCustomerModule {}

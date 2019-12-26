import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { CashPaymentVoucherSupplierComponent } from './cash-payment-voucher-supplier.component';
import { CashPaymentVoucherSupplierDetailComponent } from './cash-payment-voucher-supplier-detail.component';
import { CashPaymentVoucherSupplierUpdateComponent } from './cash-payment-voucher-supplier-update.component';
import { CashPaymentVoucherSupplierDeleteDialogComponent } from './cash-payment-voucher-supplier-delete-dialog.component';
import { cashPaymentVoucherSupplierRoute } from './cash-payment-voucher-supplier.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(cashPaymentVoucherSupplierRoute)],
  declarations: [
    CashPaymentVoucherSupplierComponent,
    CashPaymentVoucherSupplierDetailComponent,
    CashPaymentVoucherSupplierUpdateComponent,
    CashPaymentVoucherSupplierDeleteDialogComponent
  ],
  entryComponents: [CashPaymentVoucherSupplierDeleteDialogComponent]
})
export class WikunumCashPaymentVoucherSupplierModule {}

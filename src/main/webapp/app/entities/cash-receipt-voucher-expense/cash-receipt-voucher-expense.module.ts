import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { CashReceiptVoucherExpenseComponent } from './cash-receipt-voucher-expense.component';
import { CashReceiptVoucherExpenseDetailComponent } from './cash-receipt-voucher-expense-detail.component';
import { CashReceiptVoucherExpenseUpdateComponent } from './cash-receipt-voucher-expense-update.component';
import { CashReceiptVoucherExpenseDeleteDialogComponent } from './cash-receipt-voucher-expense-delete-dialog.component';
import { cashReceiptVoucherExpenseRoute } from './cash-receipt-voucher-expense.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(cashReceiptVoucherExpenseRoute)],
  declarations: [
    CashReceiptVoucherExpenseComponent,
    CashReceiptVoucherExpenseDetailComponent,
    CashReceiptVoucherExpenseUpdateComponent,
    CashReceiptVoucherExpenseDeleteDialogComponent
  ],
  entryComponents: [CashReceiptVoucherExpenseDeleteDialogComponent]
})
export class WikunumCashReceiptVoucherExpenseModule {}

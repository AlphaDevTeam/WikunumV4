import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { PaymentTypeBalanceComponent } from './payment-type-balance.component';
import { PaymentTypeBalanceDetailComponent } from './payment-type-balance-detail.component';
import { PaymentTypeBalanceUpdateComponent } from './payment-type-balance-update.component';
import { PaymentTypeBalanceDeleteDialogComponent } from './payment-type-balance-delete-dialog.component';
import { paymentTypeBalanceRoute } from './payment-type-balance.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(paymentTypeBalanceRoute)],
  declarations: [
    PaymentTypeBalanceComponent,
    PaymentTypeBalanceDetailComponent,
    PaymentTypeBalanceUpdateComponent,
    PaymentTypeBalanceDeleteDialogComponent
  ],
  entryComponents: [PaymentTypeBalanceDeleteDialogComponent]
})
export class WikunumPaymentTypeBalanceModule {}

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { PaymentTypeAccountComponent } from './payment-type-account.component';
import { PaymentTypeAccountDetailComponent } from './payment-type-account-detail.component';
import { PaymentTypeAccountUpdateComponent } from './payment-type-account-update.component';
import { PaymentTypeAccountDeleteDialogComponent } from './payment-type-account-delete-dialog.component';
import { paymentTypeAccountRoute } from './payment-type-account.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(paymentTypeAccountRoute)],
  declarations: [
    PaymentTypeAccountComponent,
    PaymentTypeAccountDetailComponent,
    PaymentTypeAccountUpdateComponent,
    PaymentTypeAccountDeleteDialogComponent
  ],
  entryComponents: [PaymentTypeAccountDeleteDialogComponent]
})
export class WikunumPaymentTypeAccountModule {}

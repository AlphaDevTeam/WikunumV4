import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { PaymentTypesComponent } from './payment-types.component';
import { PaymentTypesDetailComponent } from './payment-types-detail.component';
import { PaymentTypesUpdateComponent } from './payment-types-update.component';
import { PaymentTypesDeleteDialogComponent } from './payment-types-delete-dialog.component';
import { paymentTypesRoute } from './payment-types.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(paymentTypesRoute)],
  declarations: [PaymentTypesComponent, PaymentTypesDetailComponent, PaymentTypesUpdateComponent, PaymentTypesDeleteDialogComponent],
  entryComponents: [PaymentTypesDeleteDialogComponent]
})
export class WikunumPaymentTypesModule {}

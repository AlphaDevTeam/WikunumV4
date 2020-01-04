import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { TransactionTypeComponent } from './transaction-type.component';
import { TransactionTypeDetailComponent } from './transaction-type-detail.component';
import { TransactionTypeUpdateComponent } from './transaction-type-update.component';
import { TransactionTypeDeleteDialogComponent } from './transaction-type-delete-dialog.component';
import { transactionTypeRoute } from './transaction-type.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(transactionTypeRoute)],
  declarations: [
    TransactionTypeComponent,
    TransactionTypeDetailComponent,
    TransactionTypeUpdateComponent,
    TransactionTypeDeleteDialogComponent
  ],
  entryComponents: [TransactionTypeDeleteDialogComponent]
})
export class WikunumTransactionTypeModule {}

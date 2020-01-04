import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { InvoiceDetailsComponent } from './invoice-details.component';
import { InvoiceDetailsDetailComponent } from './invoice-details-detail.component';
import { InvoiceDetailsUpdateComponent } from './invoice-details-update.component';
import { InvoiceDetailsDeleteDialogComponent } from './invoice-details-delete-dialog.component';
import { invoiceDetailsRoute } from './invoice-details.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(invoiceDetailsRoute)],
  declarations: [
    InvoiceDetailsComponent,
    InvoiceDetailsDetailComponent,
    InvoiceDetailsUpdateComponent,
    InvoiceDetailsDeleteDialogComponent
  ],
  entryComponents: [InvoiceDetailsDeleteDialogComponent]
})
export class WikunumInvoiceDetailsModule {}

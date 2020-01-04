import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { QuotationDetailsComponent } from './quotation-details.component';
import { QuotationDetailsDetailComponent } from './quotation-details-detail.component';
import { QuotationDetailsUpdateComponent } from './quotation-details-update.component';
import { QuotationDetailsDeleteDialogComponent } from './quotation-details-delete-dialog.component';
import { quotationDetailsRoute } from './quotation-details.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(quotationDetailsRoute)],
  declarations: [
    QuotationDetailsComponent,
    QuotationDetailsDetailComponent,
    QuotationDetailsUpdateComponent,
    QuotationDetailsDeleteDialogComponent
  ],
  entryComponents: [QuotationDetailsDeleteDialogComponent]
})
export class WikunumQuotationDetailsModule {}

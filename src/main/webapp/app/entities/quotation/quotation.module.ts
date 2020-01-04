import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { QuotationComponent } from './quotation.component';
import { QuotationDetailComponent } from './quotation-detail.component';
import { QuotationUpdateComponent } from './quotation-update.component';
import { QuotationDeleteDialogComponent } from './quotation-delete-dialog.component';
import { quotationRoute } from './quotation.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(quotationRoute)],
  declarations: [QuotationComponent, QuotationDetailComponent, QuotationUpdateComponent, QuotationDeleteDialogComponent],
  entryComponents: [QuotationDeleteDialogComponent]
})
export class WikunumQuotationModule {}

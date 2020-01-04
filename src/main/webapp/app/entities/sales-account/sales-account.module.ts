import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { SalesAccountComponent } from './sales-account.component';
import { SalesAccountDetailComponent } from './sales-account-detail.component';
import { SalesAccountUpdateComponent } from './sales-account-update.component';
import { SalesAccountDeleteDialogComponent } from './sales-account-delete-dialog.component';
import { salesAccountRoute } from './sales-account.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(salesAccountRoute)],
  declarations: [SalesAccountComponent, SalesAccountDetailComponent, SalesAccountUpdateComponent, SalesAccountDeleteDialogComponent],
  entryComponents: [SalesAccountDeleteDialogComponent]
})
export class WikunumSalesAccountModule {}

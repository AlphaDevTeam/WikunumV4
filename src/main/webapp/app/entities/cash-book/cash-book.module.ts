import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { CashBookComponent } from './cash-book.component';
import { CashBookDetailComponent } from './cash-book-detail.component';
import { CashBookUpdateComponent } from './cash-book-update.component';
import { CashBookDeleteDialogComponent } from './cash-book-delete-dialog.component';
import { cashBookRoute } from './cash-book.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(cashBookRoute)],
  declarations: [CashBookComponent, CashBookDetailComponent, CashBookUpdateComponent, CashBookDeleteDialogComponent],
  entryComponents: [CashBookDeleteDialogComponent]
})
export class WikunumCashBookModule {}

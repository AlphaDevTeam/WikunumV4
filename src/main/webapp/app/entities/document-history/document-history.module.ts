import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { DocumentHistoryComponent } from './document-history.component';
import { DocumentHistoryDetailComponent } from './document-history-detail.component';
import { DocumentHistoryUpdateComponent } from './document-history-update.component';
import { DocumentHistoryDeleteDialogComponent } from './document-history-delete-dialog.component';
import { documentHistoryRoute } from './document-history.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(documentHistoryRoute)],
  declarations: [
    DocumentHistoryComponent,
    DocumentHistoryDetailComponent,
    DocumentHistoryUpdateComponent,
    DocumentHistoryDeleteDialogComponent
  ],
  entryComponents: [DocumentHistoryDeleteDialogComponent]
})
export class WikunumDocumentHistoryModule {}

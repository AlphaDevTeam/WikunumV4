import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { DocumentNumberConfigComponent } from './document-number-config.component';
import { DocumentNumberConfigDetailComponent } from './document-number-config-detail.component';
import { DocumentNumberConfigUpdateComponent } from './document-number-config-update.component';
import { DocumentNumberConfigDeleteDialogComponent } from './document-number-config-delete-dialog.component';
import { documentNumberConfigRoute } from './document-number-config.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(documentNumberConfigRoute)],
  declarations: [
    DocumentNumberConfigComponent,
    DocumentNumberConfigDetailComponent,
    DocumentNumberConfigUpdateComponent,
    DocumentNumberConfigDeleteDialogComponent
  ],
  entryComponents: [DocumentNumberConfigDeleteDialogComponent]
})
export class WikunumDocumentNumberConfigModule {}

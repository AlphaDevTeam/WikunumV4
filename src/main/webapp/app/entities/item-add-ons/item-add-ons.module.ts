import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { ItemAddOnsComponent } from './item-add-ons.component';
import { ItemAddOnsDetailComponent } from './item-add-ons-detail.component';
import { ItemAddOnsUpdateComponent } from './item-add-ons-update.component';
import { ItemAddOnsDeleteDialogComponent } from './item-add-ons-delete-dialog.component';
import { itemAddOnsRoute } from './item-add-ons.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(itemAddOnsRoute)],
  declarations: [ItemAddOnsComponent, ItemAddOnsDetailComponent, ItemAddOnsUpdateComponent, ItemAddOnsDeleteDialogComponent],
  entryComponents: [ItemAddOnsDeleteDialogComponent]
})
export class WikunumItemAddOnsModule {}

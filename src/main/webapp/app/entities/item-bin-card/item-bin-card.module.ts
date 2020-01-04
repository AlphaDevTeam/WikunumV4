import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { ItemBinCardComponent } from './item-bin-card.component';
import { ItemBinCardDetailComponent } from './item-bin-card-detail.component';
import { ItemBinCardUpdateComponent } from './item-bin-card-update.component';
import { ItemBinCardDeleteDialogComponent } from './item-bin-card-delete-dialog.component';
import { itemBinCardRoute } from './item-bin-card.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(itemBinCardRoute)],
  declarations: [ItemBinCardComponent, ItemBinCardDetailComponent, ItemBinCardUpdateComponent, ItemBinCardDeleteDialogComponent],
  entryComponents: [ItemBinCardDeleteDialogComponent]
})
export class WikunumItemBinCardModule {}

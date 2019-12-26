import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { MenuItemsComponent } from './menu-items.component';
import { MenuItemsDetailComponent } from './menu-items-detail.component';
import { MenuItemsUpdateComponent } from './menu-items-update.component';
import { MenuItemsDeleteDialogComponent } from './menu-items-delete-dialog.component';
import { menuItemsRoute } from './menu-items.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(menuItemsRoute)],
  declarations: [MenuItemsComponent, MenuItemsDetailComponent, MenuItemsUpdateComponent, MenuItemsDeleteDialogComponent],
  entryComponents: [MenuItemsDeleteDialogComponent]
})
export class WikunumMenuItemsModule {}

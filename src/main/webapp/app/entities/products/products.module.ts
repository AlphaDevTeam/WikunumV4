import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { ProductsComponent } from './products.component';
import { ProductsDetailComponent } from './products-detail.component';
import { ProductsUpdateComponent } from './products-update.component';
import { ProductsDeleteDialogComponent } from './products-delete-dialog.component';
import { productsRoute } from './products.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(productsRoute)],
  declarations: [ProductsComponent, ProductsDetailComponent, ProductsUpdateComponent, ProductsDeleteDialogComponent],
  entryComponents: [ProductsDeleteDialogComponent]
})
export class WikunumProductsModule {}

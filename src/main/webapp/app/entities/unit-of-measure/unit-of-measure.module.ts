import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { UnitOfMeasureComponent } from './unit-of-measure.component';
import { UnitOfMeasureDetailComponent } from './unit-of-measure-detail.component';
import { UnitOfMeasureUpdateComponent } from './unit-of-measure-update.component';
import { UnitOfMeasureDeleteDialogComponent } from './unit-of-measure-delete-dialog.component';
import { unitOfMeasureRoute } from './unit-of-measure.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(unitOfMeasureRoute)],
  declarations: [UnitOfMeasureComponent, UnitOfMeasureDetailComponent, UnitOfMeasureUpdateComponent, UnitOfMeasureDeleteDialogComponent],
  entryComponents: [UnitOfMeasureDeleteDialogComponent]
})
export class WikunumUnitOfMeasureModule {}

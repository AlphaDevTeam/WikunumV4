import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { EmployeeTypeComponent } from './employee-type.component';
import { EmployeeTypeDetailComponent } from './employee-type-detail.component';
import { EmployeeTypeUpdateComponent } from './employee-type-update.component';
import { EmployeeTypeDeleteDialogComponent } from './employee-type-delete-dialog.component';
import { employeeTypeRoute } from './employee-type.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(employeeTypeRoute)],
  declarations: [EmployeeTypeComponent, EmployeeTypeDetailComponent, EmployeeTypeUpdateComponent, EmployeeTypeDeleteDialogComponent],
  entryComponents: [EmployeeTypeDeleteDialogComponent]
})
export class WikunumEmployeeTypeModule {}

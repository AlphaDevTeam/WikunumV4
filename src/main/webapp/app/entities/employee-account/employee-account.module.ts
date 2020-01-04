import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { EmployeeAccountComponent } from './employee-account.component';
import { EmployeeAccountDetailComponent } from './employee-account-detail.component';
import { EmployeeAccountUpdateComponent } from './employee-account-update.component';
import { EmployeeAccountDeleteDialogComponent } from './employee-account-delete-dialog.component';
import { employeeAccountRoute } from './employee-account.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(employeeAccountRoute)],
  declarations: [
    EmployeeAccountComponent,
    EmployeeAccountDetailComponent,
    EmployeeAccountUpdateComponent,
    EmployeeAccountDeleteDialogComponent
  ],
  entryComponents: [EmployeeAccountDeleteDialogComponent]
})
export class WikunumEmployeeAccountModule {}

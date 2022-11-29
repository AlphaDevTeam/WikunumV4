import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { EmployeeAccountBalanceComponent } from './employee-account-balance.component';
import { EmployeeAccountBalanceDetailComponent } from './employee-account-balance-detail.component';
import { EmployeeAccountBalanceUpdateComponent } from './employee-account-balance-update.component';
import { EmployeeAccountBalanceDeleteDialogComponent } from './employee-account-balance-delete-dialog.component';
import { employeeAccountBalanceRoute } from './employee-account-balance.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(employeeAccountBalanceRoute)],
  declarations: [
    EmployeeAccountBalanceComponent,
    EmployeeAccountBalanceDetailComponent,
    EmployeeAccountBalanceUpdateComponent,
    EmployeeAccountBalanceDeleteDialogComponent
  ],
  entryComponents: [EmployeeAccountBalanceDeleteDialogComponent]
})
export class WikunumEmployeeAccountBalanceModule {}

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { JobStatusComponent } from './job-status.component';
import { JobStatusDetailComponent } from './job-status-detail.component';
import { JobStatusUpdateComponent } from './job-status-update.component';
import { JobStatusDeleteDialogComponent } from './job-status-delete-dialog.component';
import { jobStatusRoute } from './job-status.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(jobStatusRoute)],
  declarations: [JobStatusComponent, JobStatusDetailComponent, JobStatusUpdateComponent, JobStatusDeleteDialogComponent],
  entryComponents: [JobStatusDeleteDialogComponent]
})
export class WikunumJobStatusModule {}

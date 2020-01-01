import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { WorkerComponent } from './worker.component';
import { WorkerDetailComponent } from './worker-detail.component';
import { WorkerUpdateComponent } from './worker-update.component';
import { WorkerDeleteDialogComponent } from './worker-delete-dialog.component';
import { workerRoute } from './worker.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(workerRoute)],
  declarations: [WorkerComponent, WorkerDetailComponent, WorkerUpdateComponent, WorkerDeleteDialogComponent],
  entryComponents: [WorkerDeleteDialogComponent]
})
export class WikunumWorkerModule {}

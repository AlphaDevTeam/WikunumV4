import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikunumSharedModule } from 'app/shared/shared.module';
import { LicenseTypeComponent } from './license-type.component';
import { LicenseTypeDetailComponent } from './license-type-detail.component';
import { LicenseTypeUpdateComponent } from './license-type-update.component';
import { LicenseTypeDeleteDialogComponent } from './license-type-delete-dialog.component';
import { licenseTypeRoute } from './license-type.route';

@NgModule({
  imports: [WikunumSharedModule, RouterModule.forChild(licenseTypeRoute)],
  declarations: [LicenseTypeComponent, LicenseTypeDetailComponent, LicenseTypeUpdateComponent, LicenseTypeDeleteDialogComponent],
  entryComponents: [LicenseTypeDeleteDialogComponent]
})
export class WikunumLicenseTypeModule {}

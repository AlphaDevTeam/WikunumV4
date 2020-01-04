import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILicenseType } from 'app/shared/model/license-type.model';

@Component({
  selector: 'jhi-license-type-detail',
  templateUrl: './license-type-detail.component.html'
})
export class LicenseTypeDetailComponent implements OnInit {
  licenseType: ILicenseType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ licenseType }) => {
      this.licenseType = licenseType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

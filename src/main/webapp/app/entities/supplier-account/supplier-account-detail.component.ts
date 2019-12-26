import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISupplierAccount } from 'app/shared/model/supplier-account.model';

@Component({
  selector: 'jhi-supplier-account-detail',
  templateUrl: './supplier-account-detail.component.html'
})
export class SupplierAccountDetailComponent implements OnInit {
  supplierAccount: ISupplierAccount | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ supplierAccount }) => {
      this.supplierAccount = supplierAccount;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISupplierAccountBalance } from 'app/shared/model/supplier-account-balance.model';

@Component({
  selector: 'jhi-supplier-account-balance-detail',
  templateUrl: './supplier-account-balance-detail.component.html'
})
export class SupplierAccountBalanceDetailComponent implements OnInit {
  supplierAccountBalance: ISupplierAccountBalance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ supplierAccountBalance }) => {
      this.supplierAccountBalance = supplierAccountBalance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPurchaseAccountBalance } from 'app/shared/model/purchase-account-balance.model';

@Component({
  selector: 'jhi-purchase-account-balance-detail',
  templateUrl: './purchase-account-balance-detail.component.html'
})
export class PurchaseAccountBalanceDetailComponent implements OnInit {
  purchaseAccountBalance: IPurchaseAccountBalance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaseAccountBalance }) => {
      this.purchaseAccountBalance = purchaseAccountBalance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPurchaseAccount } from 'app/shared/model/purchase-account.model';

@Component({
  selector: 'jhi-purchase-account-detail',
  templateUrl: './purchase-account-detail.component.html'
})
export class PurchaseAccountDetailComponent implements OnInit {
  purchaseAccount: IPurchaseAccount | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaseAccount }) => {
      this.purchaseAccount = purchaseAccount;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

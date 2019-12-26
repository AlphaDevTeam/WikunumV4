import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISalesAccountBalance } from 'app/shared/model/sales-account-balance.model';

@Component({
  selector: 'jhi-sales-account-balance-detail',
  templateUrl: './sales-account-balance-detail.component.html'
})
export class SalesAccountBalanceDetailComponent implements OnInit {
  salesAccountBalance: ISalesAccountBalance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salesAccountBalance }) => {
      this.salesAccountBalance = salesAccountBalance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

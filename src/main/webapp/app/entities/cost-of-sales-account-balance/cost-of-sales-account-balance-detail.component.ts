import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICostOfSalesAccountBalance } from 'app/shared/model/cost-of-sales-account-balance.model';

@Component({
  selector: 'jhi-cost-of-sales-account-balance-detail',
  templateUrl: './cost-of-sales-account-balance-detail.component.html'
})
export class CostOfSalesAccountBalanceDetailComponent implements OnInit {
  costOfSalesAccountBalance: ICostOfSalesAccountBalance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ costOfSalesAccountBalance }) => {
      this.costOfSalesAccountBalance = costOfSalesAccountBalance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

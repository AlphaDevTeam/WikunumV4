import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICostOfSalesAccount } from 'app/shared/model/cost-of-sales-account.model';

@Component({
  selector: 'jhi-cost-of-sales-account-detail',
  templateUrl: './cost-of-sales-account-detail.component.html'
})
export class CostOfSalesAccountDetailComponent implements OnInit {
  costOfSalesAccount: ICostOfSalesAccount | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ costOfSalesAccount }) => {
      this.costOfSalesAccount = costOfSalesAccount;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

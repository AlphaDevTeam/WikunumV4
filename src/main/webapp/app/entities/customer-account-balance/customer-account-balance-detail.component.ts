import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomerAccountBalance } from 'app/shared/model/customer-account-balance.model';

@Component({
  selector: 'jhi-customer-account-balance-detail',
  templateUrl: './customer-account-balance-detail.component.html'
})
export class CustomerAccountBalanceDetailComponent implements OnInit {
  customerAccountBalance: ICustomerAccountBalance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerAccountBalance }) => {
      this.customerAccountBalance = customerAccountBalance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

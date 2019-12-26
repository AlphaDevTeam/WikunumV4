import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaymentTypeBalance } from 'app/shared/model/payment-type-balance.model';

@Component({
  selector: 'jhi-payment-type-balance-detail',
  templateUrl: './payment-type-balance-detail.component.html'
})
export class PaymentTypeBalanceDetailComponent implements OnInit {
  paymentTypeBalance: IPaymentTypeBalance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentTypeBalance }) => {
      this.paymentTypeBalance = paymentTypeBalance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

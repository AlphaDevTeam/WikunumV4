import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaymentTypeAccount } from 'app/shared/model/payment-type-account.model';

@Component({
  selector: 'jhi-payment-type-account-detail',
  templateUrl: './payment-type-account-detail.component.html'
})
export class PaymentTypeAccountDetailComponent implements OnInit {
  paymentTypeAccount: IPaymentTypeAccount | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentTypeAccount }) => {
      this.paymentTypeAccount = paymentTypeAccount;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

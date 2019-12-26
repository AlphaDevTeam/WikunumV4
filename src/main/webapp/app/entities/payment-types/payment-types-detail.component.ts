import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaymentTypes } from 'app/shared/model/payment-types.model';

@Component({
  selector: 'jhi-payment-types-detail',
  templateUrl: './payment-types-detail.component.html'
})
export class PaymentTypesDetailComponent implements OnInit {
  paymentTypes: IPaymentTypes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentTypes }) => {
      this.paymentTypes = paymentTypes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICashPaymentVoucherCustomer } from 'app/shared/model/cash-payment-voucher-customer.model';

@Component({
  selector: 'jhi-cash-payment-voucher-customer-detail',
  templateUrl: './cash-payment-voucher-customer-detail.component.html'
})
export class CashPaymentVoucherCustomerDetailComponent implements OnInit {
  cashPaymentVoucherCustomer: ICashPaymentVoucherCustomer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cashPaymentVoucherCustomer }) => {
      this.cashPaymentVoucherCustomer = cashPaymentVoucherCustomer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICashPaymentVoucherSupplier } from 'app/shared/model/cash-payment-voucher-supplier.model';

@Component({
  selector: 'jhi-cash-payment-voucher-supplier-detail',
  templateUrl: './cash-payment-voucher-supplier-detail.component.html'
})
export class CashPaymentVoucherSupplierDetailComponent implements OnInit {
  cashPaymentVoucherSupplier: ICashPaymentVoucherSupplier | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cashPaymentVoucherSupplier }) => {
      this.cashPaymentVoucherSupplier = cashPaymentVoucherSupplier;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

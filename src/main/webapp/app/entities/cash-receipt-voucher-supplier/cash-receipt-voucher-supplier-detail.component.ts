import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICashReceiptVoucherSupplier } from 'app/shared/model/cash-receipt-voucher-supplier.model';

@Component({
  selector: 'jhi-cash-receipt-voucher-supplier-detail',
  templateUrl: './cash-receipt-voucher-supplier-detail.component.html'
})
export class CashReceiptVoucherSupplierDetailComponent implements OnInit {
  cashReceiptVoucherSupplier: ICashReceiptVoucherSupplier | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cashReceiptVoucherSupplier }) => {
      this.cashReceiptVoucherSupplier = cashReceiptVoucherSupplier;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

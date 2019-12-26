import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICashReceiptVoucherExpense } from 'app/shared/model/cash-receipt-voucher-expense.model';

@Component({
  selector: 'jhi-cash-receipt-voucher-expense-detail',
  templateUrl: './cash-receipt-voucher-expense-detail.component.html'
})
export class CashReceiptVoucherExpenseDetailComponent implements OnInit {
  cashReceiptVoucherExpense: ICashReceiptVoucherExpense | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cashReceiptVoucherExpense }) => {
      this.cashReceiptVoucherExpense = cashReceiptVoucherExpense;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

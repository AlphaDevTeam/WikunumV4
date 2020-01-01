import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStockTransfer } from 'app/shared/model/stock-transfer.model';

@Component({
  selector: 'jhi-stock-transfer-detail',
  templateUrl: './stock-transfer-detail.component.html'
})
export class StockTransferDetailComponent implements OnInit {
  stockTransfer: IStockTransfer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stockTransfer }) => {
      this.stockTransfer = stockTransfer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

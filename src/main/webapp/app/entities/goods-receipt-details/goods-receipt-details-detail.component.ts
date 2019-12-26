import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGoodsReceiptDetails } from 'app/shared/model/goods-receipt-details.model';

@Component({
  selector: 'jhi-goods-receipt-details-detail',
  templateUrl: './goods-receipt-details-detail.component.html'
})
export class GoodsReceiptDetailsDetailComponent implements OnInit {
  goodsReceiptDetails: IGoodsReceiptDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ goodsReceiptDetails }) => {
      this.goodsReceiptDetails = goodsReceiptDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

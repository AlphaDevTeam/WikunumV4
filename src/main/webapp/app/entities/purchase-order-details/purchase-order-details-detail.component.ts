import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPurchaseOrderDetails } from 'app/shared/model/purchase-order-details.model';

@Component({
  selector: 'jhi-purchase-order-details-detail',
  templateUrl: './purchase-order-details-detail.component.html'
})
export class PurchaseOrderDetailsDetailComponent implements OnInit {
  purchaseOrderDetails: IPurchaseOrderDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaseOrderDetails }) => {
      this.purchaseOrderDetails = purchaseOrderDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

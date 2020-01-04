import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuotationDetails } from 'app/shared/model/quotation-details.model';

@Component({
  selector: 'jhi-quotation-details-detail',
  templateUrl: './quotation-details-detail.component.html'
})
export class QuotationDetailsDetailComponent implements OnInit {
  quotationDetails: IQuotationDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quotationDetails }) => {
      this.quotationDetails = quotationDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuotation } from 'app/shared/model/quotation.model';

@Component({
  selector: 'jhi-quotation-detail',
  templateUrl: './quotation-detail.component.html'
})
export class QuotationDetailComponent implements OnInit {
  quotation: IQuotation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quotation }) => {
      this.quotation = quotation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

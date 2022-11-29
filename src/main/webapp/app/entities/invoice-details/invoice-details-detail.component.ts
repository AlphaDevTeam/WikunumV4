import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInvoiceDetails } from 'app/shared/model/invoice-details.model';

@Component({
  selector: 'jhi-invoice-details-detail',
  templateUrl: './invoice-details-detail.component.html'
})
export class InvoiceDetailsDetailComponent implements OnInit {
  invoiceDetails: IInvoiceDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceDetails }) => {
      this.invoiceDetails = invoiceDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

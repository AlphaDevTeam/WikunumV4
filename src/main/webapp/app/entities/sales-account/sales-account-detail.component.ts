import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISalesAccount } from 'app/shared/model/sales-account.model';

@Component({
  selector: 'jhi-sales-account-detail',
  templateUrl: './sales-account-detail.component.html'
})
export class SalesAccountDetailComponent implements OnInit {
  salesAccount: ISalesAccount | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salesAccount }) => {
      this.salesAccount = salesAccount;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

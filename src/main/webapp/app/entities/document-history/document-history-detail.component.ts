import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentHistory } from 'app/shared/model/document-history.model';

@Component({
  selector: 'jhi-document-history-detail',
  templateUrl: './document-history-detail.component.html'
})
export class DocumentHistoryDetailComponent implements OnInit {
  documentHistory: IDocumentHistory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentHistory }) => {
      this.documentHistory = documentHistory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentNumberConfig } from 'app/shared/model/document-number-config.model';

@Component({
  selector: 'jhi-document-number-config-detail',
  templateUrl: './document-number-config-detail.component.html'
})
export class DocumentNumberConfigDetailComponent implements OnInit {
  documentNumberConfig: IDocumentNumberConfig | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentNumberConfig }) => {
      this.documentNumberConfig = documentNumberConfig;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

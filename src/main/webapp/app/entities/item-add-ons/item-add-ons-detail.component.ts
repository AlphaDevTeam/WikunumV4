import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IItemAddOns } from 'app/shared/model/item-add-ons.model';

@Component({
  selector: 'jhi-item-add-ons-detail',
  templateUrl: './item-add-ons-detail.component.html'
})
export class ItemAddOnsDetailComponent implements OnInit {
  itemAddOns: IItemAddOns | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemAddOns }) => {
      this.itemAddOns = itemAddOns;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}

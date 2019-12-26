import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItemBinCard } from 'app/shared/model/item-bin-card.model';

@Component({
  selector: 'jhi-item-bin-card-detail',
  templateUrl: './item-bin-card-detail.component.html'
})
export class ItemBinCardDetailComponent implements OnInit {
  itemBinCard: IItemBinCard | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemBinCard }) => {
      this.itemBinCard = itemBinCard;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
